import React from 'react';
import BackNavBar from 'components/common/BackNavBar/BackNavBar';
import ConcertInfo from 'components/concertCalender/ConcertInfo/ConcertInfo';
import Calender from 'components/concertCalender/Calender/Calender';
import { useParams } from 'react-router-dom';
/**
 * 여기에서 들어와야하는 데이터
 * 1. 공연포스터 => ConcertInfo
 * 2. 공연기간 | 시간 => ConcertInfo
 * 3. 공연장
 * 4. 예정되어있는 해당 공연 날짜들 => Calender
 */
function ConcertCalender() {
  const { performanceScheduleId = 0 } = useParams();
  const ID = parseInt(String(performanceScheduleId), 10) + 1;
  if (performanceScheduleId) {
    return (
      <div>
        <BackNavBar title="" />
        <ConcertInfo idx={ID} />
        <Calender idx={ID} />
      </div>
    );
  }
}

export default ConcertCalender;
