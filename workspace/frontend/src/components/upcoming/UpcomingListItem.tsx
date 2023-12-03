import React from 'react';
import {
  Box,
  Card,
  CardContent,
  CardMedia,
  Divider,
  Typography,
} from '@mui/material';

type Item = {
  id: { value: number };
  performanceScheduleList: string[];
  posterImagePath: string;
  ticketingOpenDateTime: string;
  title: string;
};

interface Props {
  item: Item;
}

const UpcomingListItem = ({ item }: Props) => {
  return (
    <Card
      sx={{
        width: '100%',
        display: 'flex',
        flexDirection: 'column',
        my: 1,
      }}
      variant="outlined"
    >
      <Box sx={{ display: 'flex', justifyContent: 'space-between', my: 1 }}>
        <CardContent sx={{ overflow: 'hidden', textOverflow: 'ellipsis' }}>
          <Typography variant="body1" noWrap>
            {item.title}
          </Typography>
        </CardContent>
        <CardMedia
          sx={{ width: '80px', height: '100px', mr: 1.5 }}
          component="img"
          image={item.posterImagePath}
          alt="img"
        />
      </Box>
      <Divider />
      <CardContent sx={{ display: 'flex', justifyContent: 'space-between' }}>
        <Typography variant="body2">티켓 오픈</Typography>
        <Typography variant="body2">
          {item.ticketingOpenDateTime.split('T')[0]}
        </Typography>
      </CardContent>
    </Card>
  );
};

export default UpcomingListItem;
