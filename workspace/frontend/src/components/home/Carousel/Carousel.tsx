import React from 'react';
import MuiCarousel from 'react-material-ui-carousel';
import CarouselItem from './CarouselItem';

interface Props {
  items: {
    id: { value: number };
    performanceScheduleList: string[];
    posterImagePath: string;
    ticketingOpenDateTime: string;
    title: string;
  }[];
}

const Carousel = ({ items }: Props) => {
  return (
    <MuiCarousel
      autoPlay
      indicators={false}
      swipe
      cycleNavigation
      navButtonsAlwaysInvisible
      fullHeightHover
    >
      {items.map((item, index) => {
        const orderedItem = { ...item, index, length: items.length };
        return <CarouselItem key={item.id.value} item={orderedItem} />;
      })}
    </MuiCarousel>
  );
};

export default Carousel;
