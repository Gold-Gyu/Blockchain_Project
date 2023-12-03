import React from 'react';
import { Box, Button, Typography, Stack } from '@mui/material';
import useMovePage from 'hooks/useMovePage';
import StackItem from './StackItem';

interface Props {
  title: string;
  items: {
    id: { value: number };
    performanceScheduleList: string[];
    posterImagePath: string;
    ticketingOpenDateTime: string;
    title: string;
  }[];
  url: string;
}

const RowStack = ({ title, items, url }: Props) => {
  const { movePage } = useMovePage();

  return (
    <Box px={1.5}>
      <Typography mt={4} mb={2} fontWeight="bold" variant="h5">
        {title}
      </Typography>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'start',
          overflowX: 'auto',
          '&::-webkit-scrollbar': { display: 'none' },
        }}
      >
        <Stack direction="row" spacing={2}>
          {items.map(item => {
            return <StackItem item={item} key={item.id.value} />;
          })}
        </Stack>
      </Box>
      <Button
        sx={{ mt: 2, mb: 4, color: '#A3A5AD' }}
        fullWidth
        variant="contained"
        color="secondary"
        onClick={() => movePage(url, null)}
        disableElevation
        size="small"
      >
        {title} 더보기
      </Button>
    </Box>
  );
};

export default RowStack;
