import useMovePage from './useMovePage';

const useUserId = () => {
  const { movePage } = useMovePage();
  const token = localStorage.getItem('accesstoken');
  if (token !== null) {
    const userId = JSON.parse(atob(token.split('.')[1])).sub;
    return userId;
  }
  return () => movePage('/login', null);
};

export default useUserId;
