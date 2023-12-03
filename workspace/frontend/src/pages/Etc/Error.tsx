import useMovePage from 'hooks/useMovePage';
import React from 'react';

const containerStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'center',
  alignItems: 'center',
  height: '100vh',
  backgroundColor: '#f2f2f2',
};

const titleStyle: React.CSSProperties = {
  fontSize: '6rem',
  fontWeight: 'bold',
  color: '#ff0000',
};

const messageStyle: React.CSSProperties = {
  fontSize: '2rem',
  marginTop: '20px',
  color: '#333',
};

const buttonStyle: React.CSSProperties = {
  marginTop: '30px',
  padding: '10px 20px',
  backgroundColor: '#007bff',
  color: '#fff',
  fontSize: '1.5rem',
  border: 'none',
  borderRadius: '5px',
  cursor: 'pointer',
  transition: 'background-color 0.3s',
};

function Error() {
  const { movePage } = useMovePage();
  const goHome = (url: string) => {
    movePage(url, null);
  };

  return (
    <div style={containerStyle}>
      <div style={titleStyle}>404</div>
      <div style={messageStyle}>Error 페이지입니다</div>
      <button type="button" onClick={() => goHome('/')} style={buttonStyle}>
        홈으로 이동하기
      </button>
    </div>
  );
}

export default Error;
