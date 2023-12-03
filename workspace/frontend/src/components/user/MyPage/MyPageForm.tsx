/* eslint-disable jsx-a11y/no-noninteractive-element-interactions */
/* eslint-disable react/button-has-type */
/* eslint-disable jsx-a11y/click-events-have-key-events */
/* eslint-disable jsx-a11y/no-static-element-interactions */
import './MyPage.scss';
import React, { useState, useEffect } from 'react';
import copyText from 'assets/CopyText.png';
import NFTCard from 'components/common/NFTCard/NFTCard';
import useAccount from 'hooks/useAccount';
import instance from 'apis/utils/instance';
import useMovePage from 'hooks/useMovePage';
import EticketJSON from 'contracts/Eticket.json';
import Web3 from 'web3';

function MyPage() {
  const { movePage } = useMovePage();
  const { account, loginMetaMask } = useAccount();
  const [metadata, setMetaData] = useState<any>([]);
  const [myAccount, setMyAccount] = useState('');
  const [userId, setUserId] = useState();
  const [userNickName, setUserNickName] = useState('닉네임');
  const [userName, setUserName] = useState('');
  const [myTicketData, setMyTicketData] = useState([]);
  const eticketJSON: any = EticketJSON;
  const web3 = new Web3(window.ethereum);

  const copyAddress = (text: string) => {
    try {
      navigator.clipboard.writeText(text);
      alert('지갑 주소가 복사되었습니다');
    } catch {
      alert('지갑 주소 복사에 실패했습니다');
    }
  };

  const handleMovePage = (index: number, reservationId: number) => {
    movePage(`/myticket/${reservationId}`, myTicketData[index]);
  };

  const getNFTId = async (userId: number) => {
    try {
      const contract = new web3.eth.Contract(
        eticketJSON.abi,
        '0xcf80f524400dd51e5875c151f61fd81beba770ed',
      );
      const NFTlist: any = await instance.get(`/api/nfts?owner=${userId}`);

      const pendingTxs = NFTlist.data.tickets.map((tokenID: any) =>
        contract.methods.tokenURI(tokenID).call({ from: myAccount }),
      );

      Promise.all(pendingTxs)
        .then(jsonUris => {
          setMetaData(jsonUris);
        })
        .catch(reason => {
          console.error('failed to fetch token data:', reason);
        });
    } catch (error) {
      console.error('nfterr', error);
    }
  };

  const getUserData = async () => {
    const token = localStorage.getItem('accesstoken');

    if (token === null) {
      movePage(`/login`, null);
    } else {
      getNFTId(JSON.parse(atob(token.split('.')[1]))['sub']);
      setUserId(JSON.parse(atob(token.split('.')[1]))['sub']);
      try {
        const userDataResponse = await instance.get(
          `/api/users/${JSON.parse(atob(token.split('.')[1]))['sub']}`,
        );
        if (userDataResponse.status === 200) {
          setUserNickName(userDataResponse.data.nickname);
          setUserName(userDataResponse.data.username);
          if (userDataResponse.data.walletAddress) {
            setMyAccount(userDataResponse.data.walletAddress);
          }
        }
      } catch (error) {
        console.error('유저 정보 호출 에러', error);
      }

      try {
        const response = await instance.get(
          `/api/tickets/${JSON.parse(atob(token.split('.')[1]))['sub']}`,
        );

        if (response.status === 200) {
          setMyTicketData(response.data);
        } else {
          alert('예매 목록을 불러오는데 실패했습니다');
        }
      } catch (error) {
        console.error('예매 정보 호출 에러', error);
      }
    }
  };

  const personalSign = async () => {
    loginMetaMask();
    if (account !== '') {
      const personalSignResult = await window.ethereum.request({
        method: 'personal_sign',
        params: [
          `I agree to register blockchain account "${account}" to Eticket account "${userName}".`,
          account,
        ],
      });
      const personalSignData = {
        personalSign: personalSignResult,
        walletAddress: account,
      };
      const personalSignVerify = await instance.post(
        `/api/users/${userId}/register-wallet`,
        personalSignData,
      );
      setMyAccount(personalSignVerify.data.walletAddress);
    }
  };

  useEffect(() => {
    getUserData();
  }, []);

  // eslint-disable-next-line react/no-unstable-nested-components
  const MyTicket = () => {
    return (
      <div className="ticket-container">
        {myTicketData.length ? (
          myTicketData.map((info: any, index: number) => {
            return (
              <div className="concert-container">
                {info.ticketStatus === 'CANCEL' ? null : (
                  <div>
                    <hr />
                    <div
                      className="my-concert"
                      onClick={() => handleMovePage(index, info.id)}
                    >
                      <div className="concert-poster">
                        <img
                          src={
                            info.performanceSchedule.performance.posterImagePath
                          }
                          alt=""
                        />
                      </div>
                      <div className="concert-info">
                        <div className="conert-info-box">
                          <div className="concert-title">
                            <h3>
                              {info.performanceSchedule.performance.title}
                            </h3>
                          </div>
                          <div className="concert-date">
                            <h4>{info.performanceSchedule.startDateTime}</h4>
                          </div>
                        </div>
                        <div className="my-ticket-arrow">
                          <h3>{'>'}</h3>
                        </div>
                      </div>
                    </div>
                  </div>
                )}
              </div>
            );
          })
        ) : (
          <div>
            <hr />
            <h3>예매 정보가 없습니다</h3>
          </div>
        )}
      </div>
    );
  };

  return (
    <div className="mypage-container">
      <div className="my-info">
        <h3 className="nickName">{userNickName}</h3>
        {myAccount === '' ? (
          <button className="edit-info" onClick={() => personalSign()}>
            <h3 className="edit-info-text">메타마스크 연결</h3>
          </button>
        ) : null}
      </div>
      {myAccount === '' ? (
        <div className="wallet">
          <h3 style={{ marginLeft: '20px' }}>메타마스크 연동이 필요합니다</h3>
        </div>
      ) : (
        <div className="wallet">
          <h3 className="my-wallet">내 지갑 : </h3>
          <h3 className="address">{myAccount}</h3>
          <img src={copyText} alt="" onClick={() => copyAddress(account)} />
        </div>
      )}
      <div className="inventory">
        <div className="my-NFT">
          <div className="upper-text">
            <h3 className="my-NFT-text">내 NFT</h3>
            <h3 className="my-NFT-more">더보기 {'>'}</h3>
          </div>
          <hr />
          <div className="NFT-list">
            {metadata.map((uri: any, index: number) => {
              if (index > 1) {
                return;
              }
              return <NFTCard uri={uri} key={index} />;
            })}
          </div>
        </div>
        <div className="my-ticket">
          <div className="upper-text">
            <h3 className="my-ticket-text">내 티켓</h3>
            <h3
              className="my-ticket-more"
              onClick={() => movePage('/receipt', null)}
            >
              구매 내역 {'>'}
            </h3>
          </div>
          <div className="my-ticket-list">
            <MyTicket />
          </div>
        </div>
      </div>
    </div>
  );
}

export default MyPage;
