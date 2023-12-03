import React from 'react';
import { Card, CardActionArea, CardMedia, Typography } from '@mui/material';
import useMovePage from 'hooks/useMovePage';

interface Props {
  item: {
    id: { value: number };
    performanceScheduleList: string[];
    posterImagePath: string;
    ticketingOpenDateTime: string;
    title: string;
    index: number;
    length: number;
  };
}

const CarouselItem = ({ item }: Props) => {
  const { movePage } = useMovePage();

  return (
    <Card
      sx={{
        position: 'relative',

        '&:after': {
          content: '""',
          display: 'block',
          position: 'absolute',
          width: '100%',
          height: '34%',
          bottom: 0,
          zIndex: 1,
          background: 'linear-gradient(to top, #000, rgba(0,0,0,0))',
        },
      }}
    >
      <Typography
        sx={{
          position: 'absolute',
          py: 0.5,
          px: 1,
          right: 10,
          bottom: 20,
          zIndex: 2,
          backgroundColor: 'rgba(255, 255, 255, 0.2)',
          color: 'white',
          borderRadius: 5,
        }}
      >{`${item.index + 1} / ${item.length}`}</Typography>
      <CardActionArea
        onClick={() => movePage(`/concert/${item.id.value}`, null)}
      >
        <CardMedia
          sx={{ borderRadius: 1 }}
          component="img"
          image={item.posterImagePath}
          alt="img"
        />
      </CardActionArea>
    </Card>
  );
};

export default CarouselItem;
