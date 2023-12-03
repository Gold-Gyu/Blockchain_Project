import React, { useEffect, useState } from 'react';
import { Button, IconButton, Toolbar } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import useMovePage from 'hooks/useMovePage';
import instance from 'apis/utils/instance';

interface Props {
  handleToggleDrawer: () => void;
}

const TopNav = ({ handleToggleDrawer }: Props) => {
  const { movePage } = useMovePage();
  const [userNickName, setUserNickName] = useState('닉네임');

  const handleMoveLogin = () => {
    handleToggleDrawer();
    movePage('/login', null);
  };

  const handleMoveMy = () => {
    handleToggleDrawer();
    movePage('/my', null);
  };

  const token = localStorage.getItem('accesstoken');
  const getUserData = async () => {
    if (token !== null) {
      try {
        const userDataResponse = await instance.get(
          `/api/users/${JSON.parse(atob(token.split('.')[1])).sub}`,
        );
        if (userDataResponse.status === 200) {
          setUserNickName(userDataResponse.data.nickname);
        }
      } catch (error) {
        console.error('유저 정보 호출 에러', error);
      }
    }
  };

  useEffect(() => {
    getUserData();
  }, []);

  return (
    <Toolbar sx={{ display: 'flex', justifyContent: 'space-between' }}>
      {!token ? (
        <Button
          size="large"
          sx={{ fontSize: '18px', textDecoration: 'underline', color: 'black' }}
          onClick={handleMoveLogin}
        >
          로그인을 해주세요.
        </Button>
      ) : (
        <Button
          size="large"
          sx={{ fontSize: '18px', textDecoration: 'underline', color: 'black' }}
          onClick={handleMoveMy}
        >
          {userNickName}
        </Button>
      )}
      <IconButton onClick={handleToggleDrawer}>
        <CloseIcon fontSize="large" />
      </IconButton>
    </Toolbar>
  );
};

export default TopNav;
