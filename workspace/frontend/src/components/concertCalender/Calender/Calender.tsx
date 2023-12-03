import React, { useEffect, useState } from 'react';
import 'react-calendar/dist/Calendar.css';
import './Calender.scss';
import Calendar from 'react-calendar';
import moment from 'moment';
import { Button } from '@mui/material';
import useMovePage from 'hooks/useMovePage';
import { useParams } from 'react-router-dom';
import instance from 'apis/utils/instance';

interface Props {
  idx: number;
}
function Calender({ idx }: Props) {
  const [, setData] = useState(null);
  const [startData, setStartData] = useState('');
  const [endData, setEndData] = useState('');
  const [dateId1, setDateId1] = useState('');
  const [dateId2, setDateId2] = useState('');
  const index = idx - 1;
  const datePart = startData.split('T')[0];
  const endDatePart = endData.split('T')[0];
  const dayList = [datePart, endDatePart];
  const { movePage } = useMovePage();
  const { performanceScheduleId } = useParams();
  const [today, setToday] = useState(new Date());
  const [selectedDayIndex, setSelectedDayIndex] = useState(0);

  useEffect(() => {
    instance
      .get(`/api/performances/${index}`)
      .then(res => {
        setData(res.data.performance);
        setStartData(
          res.data.performance.performanceScheduleList[0].startDateTime,
        );
        setEndData(
          res.data.performance.performanceScheduleList[1].startDateTime,
        );
        setDateId1(res.data.performance.performanceScheduleList[0].id);
        setDateId2(res.data.performance.performanceScheduleList[1].id);
      })
      .catch(error => console.error('Error:', error));
  }, []);

  // 선택 버튼 눌럿을 때
  const clickSelect = () => {
    if (selectedDayIndex === 0) {
      movePage(`/waiting/${performanceScheduleId}/${dateId1}`, null);
    } else {
      movePage(`/waiting/${performanceScheduleId}/${dateId2}`, null);
    }
  };

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const onCalendarChange = (date: any) => {
    if (Array.isArray(date)) {
      setToday(date[0]);

      const formattedDate = moment(date[0]).format('YYYY-MM-DD');
      setSelectedDayIndex(dayList.indexOf(formattedDate));
    } else {
      setToday(date);
      const formattedDate = moment(date).format('YYYY-MM-DD');
      setSelectedDayIndex(dayList.indexOf(formattedDate));
    }
  };

  // 사용할 수 없는 날
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const isDateDisabled = ({ date }: { date: any }) => {
    const formattedDate = moment(date).format('YYYY-MM-DD');
    return !dayList.includes(formattedDate);
  };

  return (
    <div className="calendar-outer-box">
      <div className="calendar-inner-box">
        <Calendar
          onChange={onCalendarChange}
          value={today}
          minDetail="month"
          maxDetail="month"
          next2Label={null}
          prev2Label={null}
          calendarType="hebrew"
          showNeighboringMonth={false}
          tileDisabled={isDateDisabled}
          formatDay={(_locale, date) => moment(date).format('DD')}
        />
      </div>
      <div className="selected-date">
        {moment(today).format('YYYY년 MM월 DD일')}
        <Button
          variant="contained"
          type="button"
          onClick={clickSelect}
          style={{
            background: selectedDayIndex !== -1 ? '#80C0C0' : 'gray',
            color: 'white',
            borderRadius: '20px',
            width: '100px',
            pointerEvents: selectedDayIndex !== -1 ? 'auto' : 'none',
          }}
        >
          선택
        </Button>
      </div>
      <div className="base2-line" style={{ marginTop: '20px' }} />
      <div className="date-reservation-info">
        <div className="day-ticket-info">
          <div>VIP</div>
          <div>150,000원</div>
        </div>
        <div className="base2-line" style={{ color: 'white' }} />
        <div className="day-ticket-info">
          <div>BASIC</div>
          <div>80,000원</div>
        </div>
        <div className="base2-line" style={{ color: 'white' }} />
      </div>
    </div>
  );
}

export default Calender;
