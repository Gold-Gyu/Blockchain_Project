import { assert, expect } from 'chai';
import { ethers } from 'hardhat';
import { time, mine } from '@nomicfoundation/hardhat-toolbox/network-helpers';

const MSG_PERFORMANCE_IS_NOT_SCHEDULED = /performance is not scheduled$/;
const MSG_INVALID_TOKEN_ID = /^ERC721: invalid token ID$/;
const MSG_TICKET_ALREADY_MINTED = /ticket was already minted$/;
const MSG_PERFORMANCE_ALREADY_SCHEDULED = /performance already scheduled\.$/;

describe('Eticket', function () {
  async function deployFixture() {
    const UNSCHEDULED_PERFORMANCE_ID = BigInt(1111);
    const NOW = await time.latest();

    const scheduledPerformance = {
      id: BigInt(9999),

      getTicketExpirationTime() {
        return BigInt(NOW + 100000000);
      },

      seats: {
        MINTED: 100,
        MINTED_AND_USED: 110,
        NOT_MINTED: 0,
      },
    };

    const [owner, otherAccount] = await ethers.getSigners();

    const Eticket = await ethers.getContractFactory('Eticket');
    const eticket = await Eticket.deploy('Eticket', 'Fixture');

    await eticket
      .connect(owner)
      .schedulePerformance(scheduledPerformance.id, scheduledPerformance.getTicketExpirationTime(), '', 'special url/');
    await eticket
      .connect(owner)
      .setTicketDefaultContentBaseUrl(scheduledPerformance.id, 'DEFAULT CONTENT');

    await eticket.connect(owner).mintTicket(owner.address, scheduledPerformance.id, scheduledPerformance.seats.MINTED);

    await eticket
      .connect(owner)
      .mintTicket(owner.address, scheduledPerformance.id, scheduledPerformance.seats.MINTED_AND_USED);
    await eticket.connect(owner).markTicketAsUsed(scheduledPerformance.id, scheduledPerformance.seats.MINTED_AND_USED);

    return {
      owner,
      otherAccount,
      eticket,
      UNSCHEDULED_PERFORMANCE_ID,
      scheduledPerformance,
    };
  }

  describe('Deployment', function () {
    it('Should be successfully deployed', async function () {
      assert.doesNotThrow(async () => await deployFixture());
    });
  });

  describe('Toked ID', function () {
    it('Should be composite of performance schedule id and seat id', async () => {
      const SRC_PERFORMANCE_SCHEDULE_ID = BigInt(0xaa0000bb);
      const SRC_SEAT_ID = BigInt(0xcc0000dd);

      const { eticket, otherAccount } = await deployFixture();

      const tokenId = await eticket.connect(otherAccount).makeTokenId(SRC_PERFORMANCE_SCHEDULE_ID, SRC_SEAT_ID);
      const obtainedPerformanceScheduleId = await eticket.connect(otherAccount).obtainPerformanceScheduleId(tokenId);
      const obtainedSeatId = await eticket.connect(otherAccount).obtainSeatId(tokenId);
      assert.equal(obtainedPerformanceScheduleId, SRC_PERFORMANCE_SCHEDULE_ID);
      assert.equal(obtainedSeatId, SRC_SEAT_ID);
    });
  });

  describe('Access control', function () {
    it('Should fail if non-owner account try to schedule performance', async function () {
      const { eticket, otherAccount } = await deployFixture();
      expect(eticket.connect(otherAccount).schedulePerformance(0, 0, '', '')).to.be.revertedWith(/^Ownable:.+/);
    });

    it('Should revert if non-owner account try to mint ticket', async function () {
      const { eticket, otherAccount, UNSCHEDULED_PERFORMANCE_ID } = await deployFixture();
      expect(
        eticket.connect(otherAccount).mintTicket(otherAccount.address, UNSCHEDULED_PERFORMANCE_ID, -1),
      ).to.be.revertedWith(/^Ownable:.+/);
    });

    it('Should revert when non-owner account try to mark ticket as used', async function () {
      const { eticket, otherAccount, scheduledPerformance } = await deployFixture();

      await expect(
        eticket.connect(otherAccount).markTicketAsUsed(scheduledPerformance.id, scheduledPerformance.seats.MINTED),
      ).to.be.revertedWith(/^Ownable:.+/);
    });

    it('Should revert when non-owner account try to change ticket cover uri', async function () {
      const { eticket, otherAccount, scheduledPerformance } = await deployFixture();

      await expect(eticket.connect(otherAccount).setTicketDefaultContentBaseUrl(scheduledPerformance.id, '')).to.be.revertedWith(
        /^Ownable:.+/,
      );
    });
  });

  describe('Performance schedule', function () {
    describe('Check whether performance scheduled', async function () {
      it('Should be true if performance is scheduled', async function () {
        const { eticket, owner, scheduledPerformance } = await deployFixture();
        expect(await eticket.connect(owner).isPerformanceScheduled(scheduledPerformance.id)).equals(true);
      });

      it('Should be false if performance is not scheduled', async function () {
        const { UNSCHEDULED_PERFORMANCE_ID, eticket, owner } = await deployFixture();
        expect(await eticket.connect(owner).isPerformanceScheduled(UNSCHEDULED_PERFORMANCE_ID)).equals(false);
      });
    });

    it('Should revert when performance already scheduled', async function () {
      const { eticket, owner, scheduledPerformance } = await deployFixture();
      expect(eticket.connect(owner).schedulePerformance(scheduledPerformance.id, 0, '', '')).to.be.revertedWith(MSG_PERFORMANCE_ALREADY_SCHEDULED);
    });

    it('Should schedule performance', async function () {
      const { eticket, owner, UNSCHEDULED_PERFORMANCE_ID } = await deployFixture();
      await eticket.connect(owner).schedulePerformance(UNSCHEDULED_PERFORMANCE_ID, 0, '', '');
      expect(await eticket.connect(owner).isPerformanceScheduled(UNSCHEDULED_PERFORMANCE_ID)).equals(true);
    });

    it('Should return performance end time', async function () {
      const { eticket, otherAccount, scheduledPerformance } = await deployFixture();
      expect(await eticket.connect(otherAccount).ticketExpirationTimeOf(scheduledPerformance.id)).equals(
        scheduledPerformance.getTicketExpirationTime(),
      );
    });
  });

  describe('Ticket', async function () {
    describe('Query ticket state', async function () {
      it('Should revert when performance schedule does not exist', async function () {
        const { UNSCHEDULED_PERFORMANCE_ID, eticket, otherAccount } = await deployFixture();

        await expect(eticket.connect(otherAccount).isTicketMinted(UNSCHEDULED_PERFORMANCE_ID, 0)).to.be.revertedWith(
          MSG_PERFORMANCE_IS_NOT_SCHEDULED,
        );
      });

      it('Should be false if ticket is not minted', async function () {
        const { eticket, otherAccount, scheduledPerformance } = await deployFixture();

        expect(
          await eticket
            .connect(otherAccount)
            .isTicketMinted(scheduledPerformance.id, scheduledPerformance.seats.NOT_MINTED),
        ).equals(false);
      });

      it('Should be true if ticket is minted', async function () {
        const { eticket, otherAccount, scheduledPerformance } = await deployFixture();

        expect(
          await eticket
            .connect(otherAccount)
            .isTicketMinted(scheduledPerformance.id, scheduledPerformance.seats.MINTED),
        ).equals(true);
      });

      it('Should return owner address', async function () {
        const { eticket, owner, otherAccount, scheduledPerformance } = await deployFixture();

        expect(
          await eticket.connect(otherAccount).ticketOwnerOf(scheduledPerformance.id, scheduledPerformance.seats.MINTED),
        ).equals(owner.address);
      });
    });

    describe('Change ticket cover image', () => {
      it('Should revert when performance is not scheduled', async function () {
        const { eticket, owner, UNSCHEDULED_PERFORMANCE_ID } = await deployFixture();

        await expect(eticket.connect(owner).setTicketDefaultContentBaseUrl(UNSCHEDULED_PERFORMANCE_ID, '')).to.be.revertedWith(
          MSG_PERFORMANCE_IS_NOT_SCHEDULED,
        );
      });

      it('Should change cover uri', async function () {
        const DUMMY_COVER_URI = 'hello, from ETICKET!';

        const { eticket, owner, otherAccount, scheduledPerformance } = await deployFixture();

        await eticket.connect(owner).setTicketDefaultContentBaseUrl(scheduledPerformance.id, DUMMY_COVER_URI);
        expect(await eticket.connect(otherAccount).getTicketDefaultContentBaseUrl(scheduledPerformance.id)).equals(DUMMY_COVER_URI);
      });
    });

    describe('Usage state management', async function () {
      describe('Check ticket usage state', async function () {
        it('Should revert when ticket is not minted', async function () {
          const { eticket, otherAccount, scheduledPerformance } = await deployFixture();

          await expect(
            eticket.connect(otherAccount).isTicketUsed(scheduledPerformance.id, scheduledPerformance.seats.NOT_MINTED),
          ).to.be.revertedWith(MSG_INVALID_TOKEN_ID);
        });

        it('Should be false when ticket is not used', async function () {
          const { eticket, otherAccount, scheduledPerformance } = await deployFixture();

          expect(
            await eticket
              .connect(otherAccount)
              .isTicketUsed(scheduledPerformance.id, scheduledPerformance.seats.MINTED),
          ).equals(false);
        });

        it('Should be true when ticket is used', async function () {
          const { eticket, otherAccount, scheduledPerformance } = await deployFixture();

          expect(
            await eticket
              .connect(otherAccount)
              .isTicketUsed(scheduledPerformance.id, scheduledPerformance.seats.MINTED_AND_USED),
          ).equals(true);
        });
      });

      describe('Mark ticket as used', async function () {
        it('Should revert when performance does not exist', async function () {
          const { eticket, owner, UNSCHEDULED_PERFORMANCE_ID } = await deployFixture();

          await expect(eticket.connect(owner).markTicketAsUsed(UNSCHEDULED_PERFORMANCE_ID, 0)).to.be.revertedWith(
            MSG_PERFORMANCE_IS_NOT_SCHEDULED,
          );
        });

        it('Should revert when ticket is not minted', async function () {
          const { eticket, owner, scheduledPerformance } = await deployFixture();

          await expect(
            eticket.connect(owner).markTicketAsUsed(scheduledPerformance.id, scheduledPerformance.seats.NOT_MINTED),
          ).to.be.revertedWith(MSG_INVALID_TOKEN_ID);
        });

        it('Should mark ticket as used', async function () {
          const { eticket, owner, otherAccount, scheduledPerformance } = await deployFixture();

          await eticket.connect(owner).markTicketAsUsed(scheduledPerformance.id, scheduledPerformance.seats.MINTED);
          expect(
            await eticket
              .connect(otherAccount)
              .isTicketUsed(scheduledPerformance.id, scheduledPerformance.seats.MINTED),
          ).equals(true);
        });
      });
    });

    describe('Mint ticket', async function () {
      it('Should revert when performance is not scheduled', async function () {
        const { UNSCHEDULED_PERFORMANCE_ID, eticket, owner } = await deployFixture();

        expect(eticket.connect(owner).mintTicket(owner.address, UNSCHEDULED_PERFORMANCE_ID, 0)).to.be.revertedWith(
          MSG_PERFORMANCE_IS_NOT_SCHEDULED,
        );
      });

      it('Should revert when ticket was already minted', async function () {
        const { eticket, owner, scheduledPerformance } = await deployFixture();

        expect(
          eticket.connect(owner).mintTicket(owner.address, scheduledPerformance.id, scheduledPerformance.seats.MINTED),
        ).to.be.revertedWith(MSG_TICKET_ALREADY_MINTED);
      });

      it('Should mint ticket', async function () {
        const { eticket, owner, otherAccount, scheduledPerformance } = await deployFixture();

        await eticket
          .connect(owner)
          .mintTicket(otherAccount.address, scheduledPerformance.id, scheduledPerformance.seats.NOT_MINTED);

        const tokenId = await eticket
          .connect(owner)
          .makeTokenId(scheduledPerformance.id, scheduledPerformance.seats.NOT_MINTED);

        expect(await eticket.connect(owner).ownerOf(tokenId)).equals(otherAccount.address);
      });
    });

    describe('Transition of token', function () {
      it('Should return default cover uri before expiration', async function () {
        const { eticket, otherAccount, scheduledPerformance } = await deployFixture();

        const tokenId = await eticket
          .connect(otherAccount)
          .makeTokenId(scheduledPerformance.id, scheduledPerformance.seats.MINTED);

        expect(await eticket.connect(otherAccount).tokenURI(tokenId)).contains('DEFAULT CONTENT');
      });

      it('Should return default cover uri when ticket is not used even after expiration', async function () {
        const { eticket, otherAccount, scheduledPerformance } = await deployFixture();

        await time.setNextBlockTimestamp(scheduledPerformance.getTicketExpirationTime());
        await mine(1);

        const tokenId = await eticket
          .connect(otherAccount)
          .makeTokenId(scheduledPerformance.id, scheduledPerformance.seats.MINTED);

        expect(await eticket.connect(otherAccount).tokenURI(tokenId)).contains('DEFAULT CONTENT');
      });

      it('Should return special photo uri when ticket is used and expired', async function () {
        const { eticket, otherAccount, scheduledPerformance } = await deployFixture();

        await time.setNextBlockTimestamp(scheduledPerformance.getTicketExpirationTime());
        await mine(1);

        const tokenId = await eticket
          .connect(otherAccount)
          .makeTokenId(scheduledPerformance.id, scheduledPerformance.seats.MINTED_AND_USED);

        expect(await eticket.connect(otherAccount).tokenURI(tokenId)).contains('special url/');
      });
    });

    describe('Trade ticket', function () {
      it('Should fail ok?', async function () {

      });
    });
  });
});
