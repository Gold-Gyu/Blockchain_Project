import React, { useEffect, useState } from 'react';
import './Seat.scss';
import SeatSection from 'components/common/SeatSection/SeatSection';
import SeatStage from 'components/seat/SeatStage';
import Captcha from 'components/common/Captcha/Captcha';
import { Box, Modal } from '@mui/material';
import instance from 'apis/utils/instance';
import { useParams } from 'react-router-dom';

function Seat() {
  const [, setPerformanceInfo] = useState([]);
  const [openCaptcha, setOpenCaptcha] = useState<boolean>(true);
  const [seatInfo, setSeatInfo] = useState([]);
  const [title, setTitle] = useState('');
  const [location, setLocation] = useState('');
  const { seatPerformanceId = 0 } = useParams();
  const performanceId = parseInt(String(seatPerformanceId), 10);

  const getPerformances = async () => {
    try {
      const [seatRes, perRes] = await Promise.all([
        instance.get(`/api/schedules/1/sections`),
        instance.get(`/api/performances/${performanceId}`),
      ]);

      setPerformanceInfo(perRes.data.performance);
      setSeatInfo(seatRes.data.sectionList);
      setLocation(perRes.data.performance.concertHall.name);
      setTitle(perRes.data.performance.title);
    } catch (error) {
      console.error(error);
    }
  };

  const seatSections = seatInfo;

  useEffect(() => {
    getPerformances();
    instance.get('/api/test');
  }, []);

  // temp-controller로 보내기

  return (
    <div>
      <Modal open={openCaptcha}>
        <Box
          sx={{
            position: 'absolute',
            top: '50%',
            left: '50%',
            width: '70%',
            transform: 'translate(-50%, -50%)',
            bgcolor: 'background.paper',
            border: '2px solid #000',
            boxShadow: 24,
            p: 4,
          }}
        >
          <Captcha setOpenCaptcha={setOpenCaptcha} />
        </Box>
      </Modal>
      <SeatStage title={title} location={location} />
      <div className="seat-outer-box">
        <div className="seat-container">
          {seatSections.map((info, index) => (
            // eslint-disable-next-line react/no-array-index-key
            <div key={index} className="seat-section-wrapper">
              <SeatSection info={info} />
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default Seat;
