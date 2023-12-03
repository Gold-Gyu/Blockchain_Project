import React, { useEffect, useRef } from 'react';
import {
  PaymentWidgetInstance,
  loadPaymentWidget,
} from '@tosspayments/payment-widget-sdk';
import { nanoid } from 'nanoid';
import { Box, Button, Paper, Typography } from '@mui/material';
import { useLocation, useParams } from 'react-router-dom';

const selector = '#payment-widget';
const clientKey = 'test_ck_D5GePWvyJnrK0W0k6q8gLzN97Eoq';
const customerKey = 'YbX2HuSlsC9uVJW6NMRMj';

const Checkout = () => {
  const paymentWidgetRef = useRef<PaymentWidgetInstance | null>(null);
  const paymentMethodsWidgetRef = useRef<ReturnType<
    PaymentWidgetInstance['renderPaymentMethods']
  > | null>(null);
  const { checkoutPerformanceScheduleId, selectedSeatId } = useParams();
  const { state } = useLocation();
  const { price } = state;

  useEffect(() => {
    (async () => {
      const paymentWidget = await loadPaymentWidget(clientKey, customerKey);

      const paymentMethodsWidget = paymentWidget.renderPaymentMethods(
        selector,
        { value: price },
      );

      paymentWidgetRef.current = paymentWidget;
      paymentMethodsWidgetRef.current = paymentMethodsWidget;
    })();
  }, []);

  const handlePay = async () => {
    const paymentWidget = paymentWidgetRef.current;

    try {
      await paymentWidget?.requestPayment({
        orderId: nanoid(),
        orderName: '티켓',
        customerName: '203',
        customerEmail: 'customer123@gmail.com',
        successUrl: `${window.location.origin}/success/${checkoutPerformanceScheduleId}/${selectedSeatId}?price=${price}`,
        failUrl: `${window.location.origin}/fail`,
      });
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <Box>
      <Paper
        sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}
        elevation={0}
      >
        <Typography variant="h4" sx={{ mt: 5, mb: 3 }}>
          주문서
        </Typography>
        <Typography variant="h6">{`${price.toLocaleString()}원`}</Typography>
      </Paper>
      <div id="payment-widget" />
      <Button
        type="button"
        variant="contained"
        size="large"
        sx={{
          width: '90%',
          position: 'fixed',
          bottom: 30,
          color: 'white',
          left: '50%',
          transform: 'translateX(-50%)',
        }}
        onClick={handlePay}
      >
        결제하기
      </Button>
    </Box>
  );
};

export default Checkout;
