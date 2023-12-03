import React from 'react';
import SearchIcon from '@mui/icons-material/Search';
import MenuIcon from '@mui/icons-material/Menu';
import { Box, Button, IconButton } from '@mui/material';
import { useRecoilState } from 'recoil';
import hamburgerState from 'atoms/NavState';
import useMovePage from 'hooks/useMovePage';

const Buttons = () => {
  const { movePage } = useMovePage();
  const [, setHamburger] = useRecoilState(hamburgerState);
  const token = localStorage.getItem('accesstoken');
  return (
    <Box>
      {!token && (
        <Button onClick={() => movePage('/login', null)}>
          <b>로그인</b>
        </Button>
      )}
      <IconButton onClick={() => movePage('/search', null)}>
        <SearchIcon />
      </IconButton>
      <IconButton onClick={() => setHamburger(true)}>
        <MenuIcon />
      </IconButton>
    </Box>
  );
};

export default Buttons;
