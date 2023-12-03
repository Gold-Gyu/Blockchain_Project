import React from 'react';
import useSearch from 'hooks/useSearch';
import { Box, Button, Paper, Chip, Typography } from '@mui/material';

const RecentKeyword = () => {
  const { keywords, handleRemoveKeyword, handleClearKeywords } = useSearch();

  return (
    <Box>
      <Paper
        sx={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          px: 3,
          py: 2,
        }}
        elevation={0}
      >
        {keywords.length === 0 ? (
          <Typography sx={{ color: '#A2A2A2' }} variant="h6">
            검색 기록이 없습니다.
          </Typography>
        ) : (
          <>
            <Typography variant="h6" fontWeight="bold">
              최근 검색어
            </Typography>
            <Button
              type="button"
              size="large"
              onClick={handleClearKeywords}
              color="success"
            >
              전체삭제
            </Button>
          </>
        )}
      </Paper>
      <Paper
        sx={{
          display: 'flex',
          justifyContent: 'center',
          flexWrap: 'wrap',
          listStyle: 'none',
          p: 0,
          mx: 2,
          my: 0,
        }}
        component="ul"
        elevation={0}
      >
        {keywords.map(keyword => {
          return (
            <Chip
              key={keyword.id}
              sx={{ m: 1 }}
              size="medium"
              label={keyword.keyword}
              onDelete={() => {
                handleRemoveKeyword(keyword.id);
              }}
              onClick={() => {
                handleRemoveKeyword(keyword.id);
              }}
            />
          );
        })}
      </Paper>
    </Box>
  );
};

export default RecentKeyword;
