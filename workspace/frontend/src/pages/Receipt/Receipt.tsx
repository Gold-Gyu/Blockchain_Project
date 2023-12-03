import React from 'react';
import BackNavBar from 'components/common/BackNavBar/BackNavBar';
import ReceiptList from 'components/receipt/ReceiptList';

const Receipt = () => {
  return (
    <>
      <BackNavBar title="구매 내역" />
      <ReceiptList />
    </>
  );
};

export default Receipt;
