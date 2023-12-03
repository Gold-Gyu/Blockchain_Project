import React from 'react';
import useMovePage from 'hooks/useMovePage';
import { Box, Button, Typography } from '@mui/material';
import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutline';
import instance from 'apis/utils/instance';
import useUserId from 'hooks/useUserId';
import { useLocation, useParams } from 'react-router-dom';

const Success = () => {
  const { movePage } = useMovePage();
  const userId = useUserId();
  const { successPerformanceScheduleId, selectedSeatId } = useParams();
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const paymentAmount = Number(searchParams.get('price'));

  const deleteTicketing = async () => {
    try {
      await instance.delete('/api/ticketing', {
        data: {
          userId,
          performanceScheduleId: successPerformanceScheduleId,
        },
      });
    } catch (error) {
      console.error(error);
    }
  };

  const handleReserveBtnClick = async () => {
    try {
      await instance
        .post('/api/reservations', {
          performanceScheduleId: successPerformanceScheduleId,
          seatId: selectedSeatId,
          paymentAmount,
        })
        .then(() => deleteTicketing())
        .catch(error => console.error(error));
      movePage('/my', null);
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <Box
      height="100vh"
      sx={{
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
      }}
    >
      <CheckCircleOutlineIcon sx={{ fontSize: 150, mb: 3 }} color="primary" />
      <Typography variant="h5">결제가 완료되었습니다!</Typography>
      <Button
        sx={{
          width: '90%',
          position: 'fixed',
          bottom: 20,
          color: 'white',
          fontSize: '20px',
        }}
        variant="contained"
        onClick={handleReserveBtnClick}
      >
        확인
      </Button>
    </Box>
  );
};

export default Success;
