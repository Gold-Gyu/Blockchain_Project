import React, { useState } from 'react';
import { useRecoilState } from 'recoil';
import './SeatBox.scss';
import SelectSeatState from 'atoms/SelectSeatState';
import SeatId from 'atoms/SeatId';

interface stateType {
  id: number;
  row: null;
  number: string;
  seatStatus: string;
}

function SeatBox({ index, state }: { index: number; state: stateType }) {
  const [isSelected, setIsSelected] = useState(false);
  const [, setSelectedSeats] = useRecoilState(SelectSeatState);
  const [, setSelectSeatId] = useRecoilState<number | null>(SeatId);
  const handleClick = () => {
    // 인덱스 값이 0일 때만 클릭 가능하도록
    if (state.seatStatus === 'ONSALE') {
      setIsSelected(!isSelected);
      setSelectSeatId(state.id);
      // 선택한 좌state.id을 추가 또는 제거
      if (isSelected) {
        setSelectedSeats(prevSelectedSeats =>
          prevSelectedSeats.filter(seat => seat !== index),
        );
      } else {
        setSelectedSeats(prevSelectedSeats => [...prevSelectedSeats, index]);
      }
    }
  };

  // 인덱스 값에 따라 색상 지정
  const boxStyle = {
    backgroundColor:
      // eslint-disable-next-line no-nested-ternary
      state.seatStatus !== 'ONSALE' ? '#ccc' : isSelected ? '#80c0c0' : 'white',
    color: isSelected && state.seatStatus !== 'ONSALE' ? 'white' : 'black',
    cursor: state.seatStatus === 'ONSALE' ? 'pointer' : 'not-allowed',
  };

  return (
    <div>
      <div>
        <div
          className="seat-box"
          style={boxStyle}
          onClick={handleClick}
          aria-hidden
        />
      </div>
    </div>
  );
}

export default SeatBox;
