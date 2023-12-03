import React, { useState, useEffect } from 'react';
import './LoginForm.scss';
import { Button, TextField } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import Eticket from 'assets/ETICKET.svg';
import useMovePage from 'hooks/useMovePage';
import instance from 'apis/utils/instance';
import siwe from 'hooks/siwe';
import metamaskImg from 'assets/MetaMask.png';

interface loginDataTyoe {
  username: string;
  password: string;
}

function LoginForm() {
  const { movePage } = useMovePage();
  // 이동 로직
  const navigate = useNavigate();
  // 아이디 데이터 상태 선언
  const [usernameData, setUsernameData] = useState('');
  // 비밀번호 데이터
  const [passwordDadta, setPasswordData] = useState('');
  // 아이디 정보 실시간 저장
  const getUsernameData = (event: React.ChangeEvent<HTMLInputElement>) => {
    setUsernameData(event.target.value);
  };
  // 비밀번호 실시간 저장
  const getPasswordData = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPasswordData(event.target.value);
  };

  const { getSIWE } = siwe();

  const handleSigninBtnClick = async () => {
    try {
      const loginData: loginDataTyoe = {
        username: usernameData,
        password: passwordDadta,
      };

      const response = await instance.post(`/api/auth/signin`, loginData, {
        headers: {
          'X-Authentication-Strategy': 'basic',
        },
      }); // POST 요청으로 변경
      if (response.status === 200) {
        localStorage.setItem('accesstoken', response.data.accessToken);
        movePage('/', null);
      }
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    if (localStorage.getItem('accesstoken')) {
      movePage('/', null);
    }
  }, []);

  return (
    <div className="login-box">
      <div className="login-outer-box">
        <div className="logo-box" onClick={() => navigate('/')}>
          <img src={Eticket} alt="" />
        </div>
        <div className="page-name-title">로그인</div>
        <div>아이디</div>
        <TextField fullWidth id="fullWidth" onChange={getUsernameData} />
        <div>비밀번호</div>
        <TextField
          id="outlined-password-input"
          type="password"
          autoComplete="current-password"
          onChange={getPasswordData}
          sx={{ width: '100%' }} // 가로 전체 너비 스타일을 추가
        />
        <div className="signup-bottom-box">
          <div className="check-login">
            <div
              className="text-login-underline"
              onClick={() => navigate('/signup')}
              aria-hidden="true"
            >
              회원가입
            </div>
          </div>

          <div>
            <Button
              variant="contained"
              type="button"
              onClick={handleSigninBtnClick}
              style={{ background: '#80C0C0', color: 'white' }}
            >
              로그인
            </Button>
          </div>
        </div>
        <hr style={{ width: '100%' }} />
        <Button
          variant="contained"
          type="button"
          style={{
            background: '#F2F4F6',
            color: '#80C0C0',
            width: '100%',
            height: '50px',
          }}
        >
          <div
            className="metamask-logo-contain"
            onClick={() => getSIWE()}
            aria-hidden
          >
            <div className="meta-logo-text">
              <h3 style={{ margin: '0' }}>메타마스크로 로그인</h3>
            </div>
            <div className="meta-logo-box">
              <img
                src={metamaskImg}
                alt="메타마스크"
                style={{ width: '30px', height: '30px' }}
              />
            </div>
          </div>
        </Button>
      </div>
    </div>
  );
}

export default LoginForm;
