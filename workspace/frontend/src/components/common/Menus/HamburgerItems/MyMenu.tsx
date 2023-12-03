import React from 'react';
import {
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
} from '@mui/material';
import HomeIcon from '@mui/icons-material/Home';
import ReceiptIcon from '@mui/icons-material/Receipt';
import PersonIcon from '@mui/icons-material/Person';
import useMovePage from 'hooks/useMovePage';

interface Props {
  handleToggleDrawer: () => void;
}

const MyMenu = ({ handleToggleDrawer }: Props) => {
  const menus = [
    { name: '홈', url: '/' },
    { name: '구매 내역', url: '/receipt' },
    { name: '마이 페이지', url: '/my' },
  ];

  const { movePage } = useMovePage();

  const handleMovePage = (url: string) => {
    handleToggleDrawer();
    movePage(url, null);
  };

  return (
    <List disablePadding>
      {menus.map((menu, index) => (
        <ListItem key={menu.name}>
          <ListItemButton onClick={() => handleMovePage(menu.url)}>
            <ListItemIcon>
              {index === 0 && <HomeIcon />}
              {index === 1 && <ReceiptIcon />}
              {index === 2 && <PersonIcon />}
            </ListItemIcon>
            <ListItemText
              primary={menu.name}
              primaryTypographyProps={{
                fontSize: 20,
                fontWeight: 'medium',
              }}
            />
          </ListItemButton>
        </ListItem>
      ))}
    </List>
  );
};

export default MyMenu;
