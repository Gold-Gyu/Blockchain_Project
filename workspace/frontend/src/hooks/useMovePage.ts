import { useNavigate } from 'react-router-dom';

const useMovePage = () => {
  const navigate = useNavigate();

  const movePage = (url: string, state: object | null) => {
    navigate(url, { state });
  };
  const goBack = () => {
    navigate(-1);
  };
  return { movePage, goBack };
};

export default useMovePage;
