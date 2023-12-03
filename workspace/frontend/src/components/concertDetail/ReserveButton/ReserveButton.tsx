import React from 'react';
import './ReserveButton.scss';
import { Button } from '@mui/material';
import { useParams } from 'react-router-dom';
import useMovePage from 'hooks/useMovePage';
import instance from 'apis/utils/instance';

function ReserveButton() {
  const { movePage } = useMovePage();
  const { performanceId } = useParams();
  const token = localStorage.getItem('accesstoken');
  const moveReservePage = async () => {
    // eslint-disable-next-line no-unused-expressions
    // token
    //   ? movePage(`/ConcertCalender/${performanceId}`, null)
    //   : movePage(`/login`, null);

    if (token === null) {
      alert('로그인이 필요합니다');
      movePage(`/login`, null);
    } else {
      try {
        const userDataResponse = await instance.get(
          `/api/users/${JSON.parse(atob(token.split('.')[1]))['sub']}`,
        );
        if (userDataResponse.status === 200) {
          if (userDataResponse.data.walletAddress) {
            movePage(`/ConcertCalender/${performanceId}`, null);
          } else {
            alert('마타마스크 연동이 필요합니다');
            movePage(`/my`, null);
          }
        }
      } catch (error) {
        console.error('유저 정보 호출 에러', error);
      }
    }

    // movePage(`/ConcertCalender/${performanceId}`, null);
  };
  return (
    <div className="reservation-btn">
      <Button
        variant="contained"
        type="button"
        onClick={moveReservePage}
        style={{
          background: '#80C0C0',
          color: 'white',
          width: '100%',
          height: '55px',
          position: 'fixed', // 화면 하단에 고정
          bottom: '0px', // 하단 여백 조절
          left: '50%', // 가운데 정렬
          transform: 'translateX(-50%)', // 가운데 정렬,
          zIndex: '1000', // 다른 요소 위에 표시
          maxWidth: '500px',
        }}
      >
        <h3>예매하기</h3>
      </Button>
    </div>
  );
}

export default ReserveButton;
