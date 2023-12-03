/* eslint-disable consistent-return */
/* eslint-disable array-callback-return */
/* eslint-disable react/button-has-type */
import './MyTicketDetail.scss';
import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import QRcode from 'qrcode.react';
import { Box, Typography, Modal } from '@mui/material';
import instance from 'apis/utils/instance';
import useMovePage from 'hooks/useMovePage';
import SlidingImage from './SlidingImage';

function MyTicket() {
  const { movePage } = useMovePage();
  const [myTicketData, setMyTicketData] = useState([]);
  const { idx } = useParams();
  const [isFlipped, setIsFlipped] = useState(false);
  const [rotate, setRotate] = useState(0);
  const [openCancelModal, setOpenCancelModal] = useState(false);

  // 예매 취소 api 연결필요
  const { goBack } = useMovePage();

  const getUserData = async () => {
    const token = localStorage.getItem('accesstoken');

    if (token === null) {
      movePage(`/login`, null);
    } else {
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

  const imageFlip = () => {
    if (isFlipped) {
      setRotate(0);
      setIsFlipped(false);
    } else {
      setRotate(180);
      setIsFlipped(true);
    }
  };

  const askCancellReservation = () => {
    setOpenCancelModal(true);
  };

  const closeCancelModal = () => {
    setOpenCancelModal(false);
  };

  const cancellReservation = async () => {
    try {
      const response = await instance.put(`/api/reservations/${idx}`);

      if (response.status === 200) {
        alert('예매 취소되었습니다');
        goBack();
      }
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    getUserData();
  }, []);

  return (
    <div className="container">
      {myTicketData.map((info: any) => {
        if (info.id === Number(idx)) {
          return (
            <div className="my-ticket-detail">
              <div className="my-ticket-info">
                <h3>{info.performanceSchedule.performance.title}</h3>
                <h3>{info.performanceSchedule.startDateTime}</h3>
              </div>
              <div className="slide-image">
                <SlidingImage />
              </div>
              <div className="my-ticket-image">
                <div
                  className="card-inner"
                  style={{ transform: `rotateY(${rotate}deg)` }}
                >
                  <div className="card-front">
                    <img
                      src={info.performanceSchedule.performance.posterImagePath}
                      alt=""
                    />
                  </div>
                  <div className="card-back">
                    <div className="QRcode">
                      <QRcode
                        id="myqr"
                        value={JSON.stringify(info)}
                        size={320}
                        includeMargin
                      />
                    </div>
                  </div>
                </div>
                {isFlipped ? (
                  <button onClick={imageFlip}>
                    <h3>포스터</h3>
                  </button>
                ) : (
                  <button onClick={imageFlip}>
                    <h3>QR 코드 생성</h3>
                  </button>
                )}
              </div>
              <div className="cancel-reservation">
                <button onClick={() => askCancellReservation()}>
                  <h3>예매 취소</h3>
                </button>
              </div>
            </div>
          );
        }
      })}
      <Modal open={openCancelModal}>
        <Box
          sx={{
            position: 'absolute',
            top: '50%',
            left: '50%',
            width: '70%',
            transform: 'translate(-50%, -50%)',
            bgcolor: 'background.paper',
            border: '0px',
            boxShadow: 24,
            p: 4,
          }}
        >
          <Typography id="modal-modal-title" variant="h6" component="h3">
            정말 취소하시겠습니까?
          </Typography>
          <div className="ask-cancel">
            <button className="ask-yes" onClick={() => cancellReservation()}>
              <h3>예</h3>
            </button>
            <button className="ask-no" onClick={() => closeCancelModal()}>
              <h3>아니요</h3>
            </button>
          </div>
        </Box>
      </Modal>
    </div>
  );
}

export default MyTicket;
