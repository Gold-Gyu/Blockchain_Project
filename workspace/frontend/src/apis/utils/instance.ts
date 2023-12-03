import axios, { AxiosError, AxiosInstance } from 'axios';
import LocalStorage from 'apis/storage/LocalStorage';

const instance: AxiosInstance = axios.create({
  headers: {
    'Content-Type': 'application/json',
  },
});

// Axios 요청시 인터셉트
instance.interceptors.request.use(req => {
  const accessToken = LocalStorage.getItem('accesstoken');
  if (accessToken) {
    req.headers.authorization = `Bearer ${accessToken}`;
  }

  return req;
});

// Axios 응답시 인터셉트
instance.interceptors.response.use(
  response => response,
  (error: AxiosError) => {
    if (error.response?.status === 401) {
      // 401 에러 처리 로직
      window.location.href = '/login';
      LocalStorage.removeItem('accesstoken');
    }
    return Promise.reject(error);
  },
);

export default instance;
