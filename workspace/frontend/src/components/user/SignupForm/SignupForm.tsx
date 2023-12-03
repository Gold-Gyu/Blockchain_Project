import React, { useState, useEffect } from 'react';
import './SignupForm.scss';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import { useNavigate } from 'react-router-dom';
// import Eticket from 'assets/ETICKET.svg';
import instance from 'apis/utils/instance';
import useMovePage from 'hooks/useMovePage';
import Logo from 'components/common/Logo/Logo';
// import { Alert } from '@mui/material';

interface signupType {
  username: string;
  password: string;
  email: string;
  nickname?: string;
  role?: string;
}

function SignupForm() {
  const { movePage } = useMovePage();
  const navigate = useNavigate();
  const [userName, setUserName] = useState('');
  const [emailData, setEmailData] = useState('');
  const [passwordData, setPasswordData] = useState('');
  const [nickName, setNickName] = useState('');
  const [userNameError, setUsernameError] = useState(false); // 이메일 형식 오류 여부 상태
  const [emailError, setEmailError] = useState(false); // 이메일 형식 오류 여부 상태
  const [passwordError, setPasswordError] = useState(false);
  const [checkPassword, setCheckPassword] = useState(false);
  // const [checkUser, setCheckUser] = useState(false);
  // useForm

  const handleEmailChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const newEmail = event.target.value;
    setEmailData(newEmail);

    // 이메일 형식을 확인하는 간단한 정규 표현식
    const emailRegex = /^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$/;
    // 이메일 형식 검증
    if (!emailRegex.test(newEmail)) {
      setEmailError(true); // 이메일 형식이 아닌 경우 에러 상태를 true로 설정
    } else {
      setEmailError(false); // 이메일 형식이 맞는 경우 에러 상태를 false로 설정
    }
  };

  const nicknameData = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNickName(e.target.value);
  };

  const handlePW = (event: React.ChangeEvent<HTMLInputElement>) => {
    const passwordRegex = /^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9]{8,}$/;

    if (
      event.target.value.length < 8 ||
      !passwordRegex.test(event.target.value)
    ) {
      setPasswordError(true);
    } else {
      setPasswordData(event.target.value);
      setPasswordError(false);
    }
  };

  const checkPW = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (passwordData !== event.target.value) {
      setCheckPassword(true);
    } else {
      setCheckPassword(false);
    }
  };

  const signupData = async () => {
    if (
      userName &&
      emailData &&
      passwordData &&
      nickName &&
      !emailError &&
      !passwordError &&
      !checkPassword
    ) {
      try {
        // POST 요청 보내기
        const userData: signupType = {
          email: emailData,
          nickname: nickName,
          password: passwordData,
          username: userName,
        };
        const response = await instance.post('/api/membership/join', userData);
        if (response.status === 201) {
          movePage('/login', null);
        }

        // 여기서 필요한 추가 로직을 수행할 수 있습니다.
      } catch (error) {
        console.error('회원가입 실패:', error);

        // 에러 처리 로직을 추가할 수 있습니다.
        throw error;
      }
    } else {
      // 모달 창으로 에러 띄우기
      console.error('에러');
    }
  };

  const validationUsername = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newUserName = e.target.value;
    const alphanumericRegex = /^[a-zA-Z][a-zA-Z0-9]*$/;

    if (alphanumericRegex.test(newUserName)) {
      setUserName(newUserName);
      setUsernameError(false);
    } else {
      // 아이디가 유효하지 않은 경우
      setUserName(newUserName); // 입력된 값을 저장 (옵션)
      setUsernameError(true); // 아이디 유효성 오류 설정
    }
  };

  useEffect(() => {
    if (localStorage.getItem('accesstoken')) {
      movePage('/', null);
    }
  }, []);

  return (
    // 회원가입 ERD
    // 이메일, 이름, 생일, role(공연주최자, 사람), 지갑주소, 닉네임,
    <div className="signup-box">
      <div className="signup-outer-box">
        <Logo />
        <div className="page-name-title">회원가입</div>
        <div>아이디</div>
        <TextField
          fullWidth
          id="fullWidth"
          onChange={validationUsername}
          error={userNameError}
          helperText={userNameError && '유효하지 않은 아이디입니다.'}
        />
        <div>비밀번호</div>
        <TextField
          id="outlined-password-input"
          type="password"
          autoComplete="current-password"
          helperText={
            passwordError && '최소 8자의 영문, 특수문자, 숫자를 넣어주세요'
          }
          error={passwordError}
          onChange={handlePW}
          sx={{ width: '100%' }} // 가로 전체 너비 스타일을 추가
        />
        <div>비밀번호 확인</div>
        <TextField
          id="outlined-password-input"
          type="password"
          autoComplete="current-password"
          helperText={checkPassword && '비밀번호를 다시 입력해주세요'}
          error={checkPassword}
          onChange={checkPW}
          sx={{ width: '100%' }} // 가로 전체 너비 스타일을 추가
        />
        <div>이메일</div>
        <Box
          sx={{
            width: 500,
            maxWidth: '100%',
          }}
        >
          <TextField
            fullWidth
            id="fullWidth"
            helperText={emailError && '이메일 형식이 올바르지 않습니다.'}
            error={emailError} // 이메일 형식 오류 여부에 따라 에러 스타일 적용
            onChange={handleEmailChange}
          />
        </Box>
        <div>닉네임</div>
        <TextField fullWidth onChange={nicknameData} />
        <div className="signup-bottom-box">
          <div className="check-login">
            {/* <div>이미 계정이 있으신가요?</div> */}
            <div
              className="text-underline"
              onClick={() => navigate('/login')}
              aria-hidden="true"
            >
              로그인
            </div>
          </div>

          <div>
            <Button
              variant="contained"
              onClick={signupData}
              style={{
                backgroundColor: '#80C0C0',
                color: 'white',
              }}
            >
              계정 만들기
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default SignupForm;
