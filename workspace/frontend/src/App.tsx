import React from 'react';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import AppRouter from './router/AppRouter.tsx';

function App() {
  const theme = createTheme({
    palette: {
      primary: {
        main: '#80C0C0',
      },
      secondary: {
        main: '#F5F5F9',
      },
    },
    typography: {
      fontFamily: [
        'Noto Sans KR',
        'Noto Sans',
        '-apple-system',
        'BlinkMacSystemFont',
        '"Segoe UI"',
        'Roboto',
        '"Helvetica Neue"',
        'Arial',
        'sans-serif',
        '"Apple Color Emoji"',
        '"Segoe UI Emoji"',
        '"Segoe UI Symbol"',
      ].join(','),
    },
  });

  return (
    <ThemeProvider theme={theme}>
      <AppRouter />
    </ThemeProvider>
  );
}

export default App;
