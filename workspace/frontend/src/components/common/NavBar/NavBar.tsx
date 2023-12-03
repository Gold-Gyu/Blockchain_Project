import React from 'react';
import Logo from 'components/common/Logo/Logo';
import Buttons from 'components/common/Buttons/Buttons';
import MenuDrawer from 'components/common/Menus/MenuDrawer';
import { Toolbar } from '@mui/material';

const NavBar = () => {
  return (
    <Toolbar sx={{ display: 'flex', justifyContent: 'space-between' }}>
      <Logo />
      <Buttons />
      <MenuDrawer />
    </Toolbar>
  );
};

export default NavBar;
