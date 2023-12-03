import './NFTDetail.scss';
import React from 'react';
import BackNavBar from 'components/common/BackNavBar/BackNavBar';
import { useLocation } from 'react-router-dom';

function NFTDetail() {
  const { state } = useLocation();

  return (
    <div>
      <BackNavBar title="" />
      <div className="NFTContainer-detail">
        <div className="NFTCard-detail">
          <div className="NFTCardImg-detail">
            <img
              src={state.image}
              alt="img"
              style={{ width: '100%', height: '100%' }}
            />
          </div>
        </div>
        <div className="NFTInfo-detail">
          <h3>이름 : {state.name}</h3>
          <h3>
            좌석 :{' '}
            {`${state.attributes[1].value}석 ${state.attributes[0].value}번`}
          </h3>
        </div>
      </div>
    </div>
  );
}

export default NFTDetail;
