import React from 'react';
import { List, ListItem, ListItemButton, ListItemText } from '@mui/material';
import useMovePage from 'hooks/useMovePage';

interface Props {
  handleToggleDrawer: () => void;
}

const AuthMenu = ({ handleToggleDrawer }: Props) => {
  const menus = [
    { name: '로그인', url: '/login' },
    { name: '회원가입', url: '/signup' },
  ];
  const logoutMenus = [{ name: '로그아웃', url: '/' }];
  const logout = () => {
    localStorage.removeItem('accesstoken');
  };
  const { movePage } = useMovePage();

  const handleMovePage = (url: string) => {
    handleToggleDrawer();
    movePage(url, null);
  };
  const Token = localStorage.getItem('accesstoken');
  return !Token ? (
    <List disablePadding>
      {menus.map(menu => (
        <ListItem key={menu.name}>
          <ListItemButton onClick={() => handleMovePage(menu.url)}>
            <ListItemText primary={menu.name} />
          </ListItemButton>
        </ListItem>
      ))}
    </List>
  ) : (
    <div onClick={logout} aria-hidden>
      <List disablePadding>
        {logoutMenus.map(menu => (
          <ListItem key={menu.name}>
            <ListItemButton onClick={() => handleMovePage(menu.url)}>
              <ListItemText primary={menu.name} />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
    </div>
  );
};

export default AuthMenu;
