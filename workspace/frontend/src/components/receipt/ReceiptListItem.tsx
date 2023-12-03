import React from 'react';
import { Box, Button, Card, CardContent, Typography } from '@mui/material';

const ReceiptListItem = ({ item }: any) => {
  const reserveDate = item.reservationTime.split('T')
  const reserveTime = reserveDate[1].split([':'])

  return (
    <Card
      sx={{
        width: '100%',
        display: 'flex',
        flexDirection: 'column',
        my: 1,
      }}
      variant="outlined"
    >
      <CardContent>
        <Typography variant="body1" noWrap>
          {item.performanceSchedule.performance.title}
        </Typography>
        <Typography variant="body2" noWrap>
          <div style={{display: 'flex', marginTop: '5px'}}>
            <p style={{margin: '0 10px 0 0'}}>결제일</p>
            <p style={{margin: '0 10px 0 0'}}>{reserveDate[0]}</p>
            <p style={{margin: '0 5px 0 0'}}>{reserveTime[0]}시</p>
            <p style={{margin: '0'}}>{reserveTime[1]}분</p>
          </div>
        </Typography>
      </CardContent>
      <Box display="flex">
        <Button fullWidth variant="contained">
          내 티켓
        </Button>
        <Button fullWidth variant="outlined">
          취소
        </Button>
      </Box>
    </Card>
  );
};

export default ReceiptListItem;
