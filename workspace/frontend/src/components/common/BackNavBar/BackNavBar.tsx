import React from 'react';
import './BackNavBar.scss';
import { Toolbar } from '@mui/material';
import BackIcon from 'assets/BackIcon.svg';
import useMovePage from 'hooks/useMovePage';

function BackNavBar({ title }: { title: string | null }) {
  const { goBack } = useMovePage();
  return (
    <Toolbar
      sx={{
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        paddingLeft: '20px',
      }}
    >
      <div onClick={() => goBack()} aria-hidden>
        <img src={BackIcon} alt="<" />
      </div>
      <div className="back-nav-title">{title}</div>
      <div />
    </Toolbar>
  );
}

export default BackNavBar;
