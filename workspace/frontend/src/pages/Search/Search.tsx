import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { Box } from '@mui/material';
import SearchInput from './SearchItems/SearchInput';
import RecentKeyword from './SearchItems/RecentKeyword';
import SearchResult from './SearchItems/SearchResult';

const Search = () => {
  const [keyword, setKeyword] = useState<string>('');
  const location = useLocation();

  useEffect(() => {
    const searchKeyword = new URLSearchParams(location.search).get('keyword');
    if (searchKeyword !== null) {
      setKeyword(searchKeyword);
    }
  }, [location]);

  return (
    <Box>
      <SearchInput />
      {keyword.length > 0 ? (
        <SearchResult keyword={keyword} />
      ) : (
        <RecentKeyword />
      )}
    </Box>
  );
};

export default Search;
