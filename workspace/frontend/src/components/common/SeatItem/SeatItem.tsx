import React, { useEffect, useState } from 'react';
import './SeatItem.scss';
import SeatBox from 'components/seat/SeatBox/SeatBox';
import { useRecoilValue } from 'recoil';
import SelectSeatState from 'atoms/SelectSeatState';
import SeatId from 'atoms/SeatId';
import useMovePage from 'hooks/useMovePage';
import { useParams } from 'react-router-dom';
import instance from 'apis/utils/instance';

interface sectionInfoType {
  id: {
    value: number;
  };
  name: string;
  seatClass: {
    className: string;
    id: {
      value: number;
    };
    price: number;
  };
  sectionSeatCount: number;
}

function SeatItem({ object }: { object: sectionInfoType }) {
  const [seatData, setSeatData] = useState([]);
  const sectionId = object.id.value;
  const { price } = object.seatClass;
  const { movePage } = useMovePage();
  const selectedSeats = useRecoilValue(SelectSeatState);
  const { seatPerformanceScheduleId } = useParams();
  const selectedSeatId = useRecoilValue(SeatId);

  const preemptVacancy = async () => {
    await instance.post(
      `/api/schedules/${seatPerformanceScheduleId}/sections/${sectionId}/seats/${selectedSeatId}`,
    );
  };

  const clickBuyBtn = () => {
    movePage(`/checkout/${seatPerformanceScheduleId}/${selectedSeatId}`, {
      price,
    });
    preemptVacancy();
  };

  useEffect(() => {
    instance
      .get(
        `/api/schedules/${seatPerformanceScheduleId}/sections/${sectionId}/vacancies`,
      )
      .then(response => {
        setSeatData(response.data.vacancies);
      })
      .catch(error => console.error('Error:', error));
  }, []);

  // eslint-disable-next-line consistent-return
  const turnAlpha = (idx: number) => {
    if (idx >= 0 && idx < 26) {
      // 0부터 25까지의 범위로 제한
      return String.fromCharCode(65 + Math.floor(idx / 5)); // 0~4: A, 5~9: B, ...
    }
  };
  // eslint-disable-next-line consistent-return

  return (
    <div>
      <div className="section-number-box">SECTION {object.name}</div>
      <div className="seat-outer-box">
        <div className="seat-item-container2">
          {Array(object.sectionSeatCount)
            .fill(null)
            .map((_, idx) => (
              // eslint-disable-next-line react/no-array-index-key
              <div key={_} className="seat-section-wrapper">
                {seatData.length > 0 && (
                  <SeatBox index={idx} state={seatData[idx]} />
                )}
              </div>
            ))}
        </div>
      </div>
      <div className="select-seat-total-area">
        <div className="section-title">좌석 선택</div>
        {selectedSeats ? (
          <div>
            {selectedSeats.map((seat, i) => (
              // eslint-disable-next-line react/no-array-index-key
              <div key={i} className="select-seat-price-box">
                <div className="selected-seat">
                  <div>
                    {turnAlpha(seat)}열 {(seat % 5) + 1}번 좌석
                  </div>
                </div>
                <div>가격: {object.seatClass.price.toLocaleString()}원</div>
              </div>
            ))}
          </div>
        ) : (
          <div className="no-selected-seats">선택한 좌석이 없습니다.</div>
        )}

        {selectedSeats && (
          <div>
            <div className="total-price">
              합계:{' '}
              {(selectedSeats.length * object.seatClass.price).toLocaleString()}
              원
            </div>
            <div className="click-buy-button" onClick={clickBuyBtn} aria-hidden>
              결제하기
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default SeatItem;
