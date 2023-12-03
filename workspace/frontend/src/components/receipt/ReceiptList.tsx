import React, { useState, useEffect } from 'react';
import { Box } from '@mui/material';
import instance from 'apis/utils/instance';
import ReceiptListItem from './ReceiptListItem';

const ReceiptList = () => {
  const [mytickets, setMyTickets] = useState<any>([]);

  const getMyTickets = async () => {
    try {
      const token = localStorage.getItem('accesstoken');
      if (token !== null) {
        const userId = JSON.parse(atob(token.split('.')[1])).sub;

        const response = await instance.get(`/api/reservations/${userId}`);
        setMyTickets(response.data);
      }
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    getMyTickets();
  }, []);

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', px: 1 }}>
      {mytickets.map((item: any) => {
        return <ReceiptListItem item={item} />;
      })}
    </Box>
  );
};

export default ReceiptList;
