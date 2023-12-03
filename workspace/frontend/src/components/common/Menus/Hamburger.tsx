import React from 'react';
import { Box, Divider } from '@mui/material';
import { useRecoilState } from 'recoil';
import hamburgerState from 'atoms/NavState';
import TopNav from './HamburgerItems/TopNav';
import MyMenu from './HamburgerItems/MyMenu';
import MainMenu from './HamburgerItems/MainMenu';
import AuthMenu from './HamburgerItems/AuthMenu';

const Hamburger = () => {
  const menus = [TopNav, MyMenu, MainMenu, AuthMenu];
  const [, setHamburger] = useRecoilState(hamburgerState);

  return (
    <div>
      {menus.map((Menu, index) => (
        <Box key={String(index)}>
          <Menu handleToggleDrawer={() => setHamburger(false)} />
          {index === 0 && <Divider />}
          {index > 0 && index < menus.length - 1 && (
            <Divider sx={{ borderBottomWidth: 15 }} />
          )}
        </Box>
      ))}
    </div>
  );
};

export default Hamburger;
