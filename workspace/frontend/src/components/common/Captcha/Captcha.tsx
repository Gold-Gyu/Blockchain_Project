import React, { Component } from 'react';
import './Captcha.scss';
import { TextField } from '@mui/material';
import BackNavBar from 'components/common/BackNavBar/BackNavBar';

import {
  loadCaptchaEnginge,
  LoadCanvasTemplate,
  validateCaptcha
} from "react-simple-captcha";

class CaptchaTest extends Component<{setOpenCaptcha: (value: boolean) => void}> {

  componentDidMount() {
    loadCaptchaEnginge(6, 'black', 'white');
  }

  doSubmit = () => {
    const user_captcha_input = document.getElementById(
      'user_captcha_input',
    ) as HTMLInputElement;
    const user_captcha = user_captcha_input.value;

    if (validateCaptcha(user_captcha) == true) {
      loadCaptchaEnginge(6, 'black', 'white');
      user_captcha_input.value = "";
      this.props.setOpenCaptcha(false)
    } else {
      alert("다시 입력해 주세요");
      user_captcha_input.value = "";
      this.props.setOpenCaptcha(true)
    }
  };

  render() {
    return (
      <div>
        <div className="header-container">
          <BackNavBar title="" />
          <h3>아래 문자를 입력해주세요</h3>
        </div>
        <div className="captcha-container">
          <div className="form-group">
            <LoadCanvasTemplate reloadText="새로고침" />
            <div className="captcha-input">
              <div>
                <TextField fullWidth id="user_captcha_input" placeholder="문자를 입력하세요"/>
              </div>
              <div className="captcha-input-button">
                <button onClick={() => this.doSubmit()}>
                  <h3>입력</h3>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default CaptchaTest;
