import React from 'react';
import BackNavBar from 'components/common/BackNavBar/BackNavBar';
import UpcomingList from 'components/upcoming/UpcomingList';

const Upcoming = () => {
  return (
    <>
      <BackNavBar title="💥예매 임박💥" />
      <UpcomingList />
    </>
  );
};

export default Upcoming;
