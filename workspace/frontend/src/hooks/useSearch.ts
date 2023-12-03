import { useEffect, useState } from 'react';

const useSearch = () => {
  const [keywords, setKeywords] = useState<{ id: number; keyword: string }[]>(
    JSON.parse(localStorage.getItem('keywords') || '[]'),
  );

  useEffect(() => {
    localStorage.setItem('keywords', JSON.stringify(keywords));
  }, [keywords]);

  const handleAddKeyword = (keyword: string) => {
    const addedKeyword = {
      id: Date.now(),
      keyword,
    };

    const keywordList = keywords.map(item => item.keyword);
    if (keywordList.includes(keyword)) {
      const filteredKeywords = keywords.filter(
        item => item.keyword !== keyword,
      );
      setKeywords([addedKeyword, ...filteredKeywords]);
    } else if (keywords.length > 29) {
      const slicedKeywords = keywords.slice(0, keywords.length - 1);
      setKeywords([addedKeyword, ...slicedKeywords]);
    } else {
      setKeywords([addedKeyword, ...keywords]);
    }
  };

  const handleRemoveKeyword = (id: number) => {
    const removedKeyword = keywords.filter(keyword => {
      return keyword.id !== id;
    });
    setKeywords(removedKeyword);
  };

  const handleClearKeywords: () => void = () => {
    setKeywords([]);
  };

  return {
    keywords,
    handleAddKeyword,
    handleRemoveKeyword,
    handleClearKeywords,
  };
};

export default useSearch;
