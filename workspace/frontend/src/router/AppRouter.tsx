import React from 'react';
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import Home from 'pages/Home/Home';
import Signup from 'pages/User/Signup';
import Login from 'pages/User/Login';
import MetamaskForm from 'pages/User/MetamaskForm';
import Search from 'pages/Search/Search';
import ConcertList from 'pages/Concert/ConcertList';
import ConcertDetail from 'pages/Concert/ConcertDetail';
import Waiting from 'pages/Waiting/Waiting';
import ConcertCalender from 'pages/Concert/ConcertCalender';
import Seat from 'pages/Seat/Seat';
import Checkout from 'pages/Checkout/Checkout';
import Success from 'components/checkout/Success';
import MyPage from 'pages/User/MyPage';
import MyTicketDetail from 'pages/User/MyTicketDetail';
import NFTGallery from 'pages/Etc/NFTGallery';
import Error from 'pages/Etc/Error';
import Upcoming from 'pages/Upcoming/Upcoming';
import Receipt from 'pages/Receipt/Receipt';
import NFTDetail from 'components/user/MyPage/MyNFT/NFTDetail';


function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/login" element={<Login />} />
        <Route path="/metamask" element={<MetamaskForm />} />
        <Route path="/search" element={<Search />} />
        <Route path="/concert" element={<ConcertList />} />
        <Route path="/concert/:performanceId" element={<ConcertDetail />} />
        <Route
          path="/concertCalender/:performanceScheduleId"
          element={<ConcertCalender />}
        />
        <Route
          path="/waiting/:waitingPerformanceId/:waitingPerformanceScheduleId"
          element={<Waiting />}
        />
        <Route
          path="/seat/:seatPerformanceId/:seatPerformanceScheduleId"
          element={<Seat />}
        />
        <Route
          path="/checkout/:checkoutPerformanceScheduleId/:selectedSeatId"
          element={<Checkout />}
        />
        <Route
          path="/success/:successPerformanceScheduleId/:selectedSeatId"
          element={<Success />}
        />
        <Route path="/upcoming" element={<Upcoming />} />
        <Route path="/receipt" element={<Receipt />} />
        <Route path="/fail" element={<Navigate replace to="/checkout" />} />
        <Route path="/my" element={<MyPage />} />
        <Route path="/myticket/:idx" element={<MyTicketDetail />} />
        <Route path="/gallery" element={<NFTGallery />} />
        <Route path="/nftdetail" element={<NFTDetail />} />
        <Route path="/*" element={<Navigate replace to="/error" />} />
        <Route path="/error" element={<Error />} />
      </Routes>
    </BrowserRouter>
  );
}

export default AppRouter;
