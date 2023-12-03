import React from 'react';
import './PriceInfo.scss';

function PriceInfo() {
  return (
    <div className="priceinfo-box">
      <div className="price-text">가격표</div>
      <div className="price-list-box">
        <div className="price-item">
          <div className="seat-type">VIP석</div>
          <div className="price-value">150,000원</div>
        </div>
        <div className="price-item">
          <div className="seat-type">BASIC석</div>
          <div className="price-value">80,000원</div>
        </div>
      </div>
    </div>
  );
}

export default PriceInfo;
