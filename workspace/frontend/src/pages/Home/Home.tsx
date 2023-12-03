import React, { useEffect, useState } from 'react';
import NavBar from 'components/common/NavBar/NavBar';
import Carousel from 'components/home/Carousel/Carousel';
import RowStack from 'components/home/RowStack/RowStack';
import { Box, Divider } from '@mui/material';
import instance from 'apis/utils/instance';

const Home = () => {
  const [hot, setHot] = useState([]);
  const [upcoming, setUpcoming] = useState([]);
  const [topFive, setTopFive] = useState([]);

  const getPerformances = async () => {
    try {
      const [hotRes, upcomingRes] = await Promise.all([
        instance.get('/api/performances/hot'),
        instance.get('/api/performances/upcoming'),
      ]);
      setHot(hotRes.data.hotPerformanceList.slice().reverse());
      setUpcoming(upcomingRes.data.upcomingPerformanceList);
      setTopFive(hotRes.data.hotPerformanceList.slice(0, 5));
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    getPerformances();
  }, []);

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column' }}>
      <NavBar />
      <Carousel items={topFive} />
      <RowStack title="공연 랭킹" items={hot} url="/concert" />
      <Divider sx={{ borderBottomWidth: 10, borderColor: '#F5F5F9' }} />
      <RowStack title="💥예매 임박💥" items={upcoming} url="/upcoming" />
    </Box>
  );
};

export default Home;
