import { atom } from 'recoil';

const SeatId = atom<number | null>({
  key: 'SeatId',
  default: null,
});

export default SeatId;
