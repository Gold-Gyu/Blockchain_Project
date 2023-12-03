import React from 'react';
import './SlidingImage.scss';
import logo from 'assets/ETICKET_logo.png';

function SlidingImage() {
  const slides = ['', '', '', '', '', '', ''];

  return (
    <div className="wrapper">
      <div className="slide_container">
        <ul className="slide_wrapper">
          <div className="slide original">
            {slides.map((s, i) => (
              <li key={i}>
                <div className="item">
                  <img src={logo} alt="" style={{ height: '50px' }} />
                  <p style={{ margin: 0 }}>{s}</p>
                </div>
              </li>
            ))}
          </div>
          <div className="slide clone">
            {slides.map((s, i) => (
              <li key={i}>
                <div className="item">
                  <img src={logo} alt="" style={{ height: '50px' }} />
                  <p style={{ margin: 0 }}>{s}</p>
                </div>
              </li>
            ))}
          </div>
        </ul>
      </div>
    </div>
  );
}

export default SlidingImage;
