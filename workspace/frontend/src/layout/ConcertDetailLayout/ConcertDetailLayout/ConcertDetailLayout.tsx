import React, { ReactNode } from 'react';
import './ConcertDetailLayout.scss';

function ConcertDetailLayout({ children }: { children: ReactNode[] }) {
  return (
    <div className="concert-container">
      <div className="concert-detail-poster-box">{children[0]}</div>
      <div className="concert-detail-content-box">{children[1]}</div>
      <div className="concert-detail-button">{children[2]}</div>
    </div>
  );
}

export default ConcertDetailLayout;
