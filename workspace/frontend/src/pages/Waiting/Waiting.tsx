import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import useMovePage from 'hooks/useMovePage';
import BackNavBar from 'components/common/BackNavBar/BackNavBar';
import {
  Box,
  LinearProgress,
  CircularProgress,
  Typography,
} from '@mui/material';
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import { Client } from '@stomp/stompjs';
import instance from 'apis/utils/instance';
import useUserId from 'hooks/useUserId';

const URL = `wss://${window.location.origin.split('//')[1]}/ws`;

const Waiting = () => {
  const [order, setOrder] = useState<number | null>(null);
  const { waitingPerformanceId, waitingPerformanceScheduleId } = useParams();
  const { movePage } = useMovePage();
  const userId = useUserId();

  const postWaiting = async () => {
    try {
      await instance.post('/api/waiting', {
        userId,
        performanceScheduleId: waitingPerformanceScheduleId,
      });
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    postWaiting();
    const client = new Client({
      brokerURL: URL,
      reconnectDelay: 5000,
      debug: str => console.log(str),
    });

    client.onConnect = () => {
      client.subscribe(`/sub/userId/${userId}`, response => {
        const message = JSON.parse(response.body);
        setOrder(Number(message.order) + 1);

        if (message.myTurn) {
          movePage(
            `/seat/${waitingPerformanceId}/${waitingPerformanceScheduleId}`,
            null,
          );
        }
      });
    };

    client.activate();

    return () => {
      client.deactivate();
    };
  }, []);

  return (
    <>
      <BackNavBar title="" />
      <Box
        sx={{
          mt: '150px',
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'center',
          alignItems: 'center',
        }}
      >
        {order ? (
          <>
            <AccessTimeIcon fontSize="large" />
            <Typography my={2} variant="h5">
              나의 대기 순서
            </Typography>
            <Typography variant="h2">{order}</Typography>
            <LinearProgress
              sx={{ width: '80%', height: 20, borderRadius: 2, my: 5 }}
            />
            <Typography variant="body1">
              현재 접속 인원이 많아 대기 중입니다.
            </Typography>
            <Typography variant="body1">
              잠시만 기다려주시면 예매 페이지로 이동합니다.
            </Typography>
            <Typography mt={10} variant="body2">
              <b>⚠주의⚠</b>
            </Typography>
            <Typography variant="body2">
              닫기, 새로고침, 뒤로가기 또는 재접속하시면
            </Typography>
            <Typography variant="body2">
              대기 순서가 초기화되어 대기 시간이 더 길어집니다.
            </Typography>
          </>
        ) : (
          <CircularProgress sx={{ mt: 10 }} size={120} />
        )}
      </Box>
    </>
  );
};

export default Waiting;
