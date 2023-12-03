import React from 'react';
import { useNavigate } from 'react-router-dom';
import { IconButton } from '@mui/material';
import iconImg from 'assets/ETICKET.svg';

const Logo = () => {
  const navigate = useNavigate();
  return (
    <div>
      <IconButton onClick={() => navigate('/')}>
        <img src={iconImg} alt="logo" />
      </IconButton>
    </div>
  );
};

export default Logo;
