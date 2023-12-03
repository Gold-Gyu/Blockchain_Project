import React from 'react';
import { Drawer } from '@mui/material';
import { useRecoilValue } from 'recoil';
import hamburgerState from 'atoms/NavState';
import Hamburger from './Hamburger';

const MenuDrawer = () => {
  const hamburger = useRecoilValue(hamburgerState);

  return (
    <Drawer
      anchor="right"
      open={hamburger}
      PaperProps={{
        sx: {
          width: '100%',
        },
      }}
    >
      <Hamburger />
    </Drawer>
  );
};

export default MenuDrawer;
