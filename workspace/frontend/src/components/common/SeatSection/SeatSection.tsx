import React, { useState, useEffect } from 'react';
import './SeatSection.scss';
import SeatItem from 'components/common/SeatItem/SeatItem';
import { useRecoilState } from 'recoil';
import SelectSeatState from 'atoms/SelectSeatState';

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

function SeatSection({ info }: { info: sectionInfoType }) {
  // const sectionIndex = info.id.value;
  const sectionClass = info.seatClass.className;

  const [isModalOpen, setModalOpen] = useState(false);
  const [, setSelectedSeats] = useRecoilState(SelectSeatState);

  const selectSeat = () => {
    setModalOpen(true);
  };

  const closeModal = () => {
    setSelectedSeats([]);
    // 모달 닫기
    setModalOpen(false);
  };

  useEffect(() => {
    const handleOutsideClick = (e: MouseEvent) => {
      if (isModalOpen) {
        const modal = document.querySelector('.modal-content');
        if (modal && !modal.contains(e.target as Node)) {
          closeModal();
        }
      }
    };

    document.addEventListener('mousedown', handleOutsideClick);

    return () => {
      document.removeEventListener('mousedown', handleOutsideClick);
    };
  }, [isModalOpen]);

  return (
    <div>
      {sectionClass === 'S' ? (
        <div className="seat-section">
          <div onClick={selectSeat} className="seat-section-box" aria-hidden>
            VIP
          </div>

          {/* 모달 */}
          {isModalOpen && (
            <div className="modal">
              <div className="modal-content">
                <SeatItem object={info} />
                <button type="button" onClick={closeModal}>
                  닫기
                </button>
              </div>
            </div>
          )}
        </div>
      ) : (
        <div className="seat-section">
          <div onClick={selectSeat} className="seat-section-box2" aria-hidden>
            BASIC
          </div>

          {/* 모달 */}
          {isModalOpen && (
            <div className="modal">
              <div className="modal-content">
                <SeatItem object={info} />
                <button type="button" onClick={closeModal}>
                  닫기
                </button>
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
}

export default SeatSection;
