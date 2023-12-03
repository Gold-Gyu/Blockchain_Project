import React from 'react';
import './SeatStage.scss';
import BackNavBar from 'components/common/BackNavBar/BackNavBar';

function SeatStage({ title, location }: { title: string; location: string }) {
  return (
    <div>
      <BackNavBar title="좌석 예매" />
      <div className="seat-stage-container">
        <div className="concert-title3">{title}</div>
        <div className="concert-location">{location}</div>
        <div className="stage-box">STAGE BOX</div>
      </div>
    </div>
  );
}

export default SeatStage;
