import React, { useEffect, useState } from 'react';
import { Box } from '@mui/material';
import instance from 'apis/utils/instance';
import UpcomingListItem from './UpcomingListItem';

type Item = {
  id: { value: number };
  performanceScheduleList: string[];
  posterImagePath: string;
  ticketingOpenDateTime: string;
  title: string;
}[];

const UpcomingList = () => {
  const [upcoming, setUpcoming] = useState<Item | []>([]);

  const getUpcoming = async () => {
    try {
      const upcomingRes = await instance.get('/api/performances/upcoming');
      setUpcoming(upcomingRes.data.upcomingPerformanceList);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    getUpcoming();
  }, []);

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', px: 1 }}>
      {upcoming.map(item => {
        return <UpcomingListItem key={item.id.value} item={item} />;
      })}
    </Box>
  );
};

export default UpcomingList;
