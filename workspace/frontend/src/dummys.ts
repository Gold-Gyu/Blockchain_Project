// import Img from 'assets/memphis.svg';
// 'https://tickets.interpark.com/contents/_next/image?url=https%3A%2F%2Fticketimage.interpark.com%2FPlay%2Fimage%2Flarge%2F23%2F23011055_p.gif&w=750&q=75',
const dummyConcerts = [
  {
    id: 0,
    image:
      'https://tickets.interpark.com/contents/_next/image?url=https%3A%2F%2Fticketimage.interpark.com%2FPlay%2Fimage%2Flarge%2F23%2F23013495_p.gif&w=750&q=75',
    title: '2023 라포엠 단독콘서트',
    concertdetail:
      'https://ticketimage.interpark.com/Play/image/etc/23/23013495-02.jpg',
    location: '올림픽공원 올림픽홀',
    date: '2023-09-09 ~ 2023-09-11',
    time: '20: 00 ~ 22: 00',
  },
  {
    id: 1,
    image:
      'https://tickets.interpark.com/contents/_next/image?url=https%3A%2F%2Fticketimage.interpark.com%2FPlay%2Fimage%2Flarge%2F23%2F23013154_p.gif&w=750&q=75',
    title: '2023 이승환 콘서트',
    location: 'SK핸드볼경기장',
    date: '2023-09-10 ~ 2023-09-14',
    concertdetail:
      'https://ticketimage.interpark.com/Play/image/etc/23/23013590-01.jpg',
    time: '20: 00 ~ 22: 00',
  },
  {
    id: 2,
    image:
      'https://tickets.interpark.com/contents/_next/image?url=https%3A%2F%2Fticketimage.interpark.com%2FPlay%2Fimage%2Flarge%2F23%2F23011055_p.gif&w=750&q=75',
    title: '그랜드 민트 페스티벌 2023',
    location: '올림픽공원',
    date: '2023-09-11 ~ 2023-09-12',
    concertdetail:
      'https://ticketimage.interpark.com/Play/image/etc/23/23011055-06.jpg',
    time: '20: 00 ~ 22: 00',
  },
  {
    id: 3,
    image:
      'https://tickets.interpark.com/contents/_next/image?url=https%3A%2F%2Fticketimage.interpark.com%2FPlay%2Fimage%2Flarge%2F23%2F23013039_p.gif&w=750&q=75',
    title: '2023서귀포글로컬페스타-제주',
    location: '제주월드컵경기장',
    date: '2023-09-11 ~ 2023-09-12',
    concertdetail:
      'https://ticketimage.interpark.com/Play/image/etc/23/23013039-09.jpg',
    time: '20: 00 ~ 22: 00',
  },
  {
    id: 4,
    image:
      'https://tickets.interpark.com/contents/_next/image?url=https%3A%2F%2Fticketimage.interpark.com%2FPlay%2Fimage%2Flarge%2F23%2F23011967_p.gif&w=750&q=75',
    title: '2023 폴킴 단독 컨서트 "남은 밤"',
    location: '올림픽공원 SK핸드볼경기장',
    date: '2023-09-11 ~ 2023-09-12',
    concertdetail:
      'https://ticketimage.interpark.com/Play/image/etc/23/23011967-05.jpg',
    time: '20: 00 ~ 22: 00',
  },
  {
    id: 5,
    image:
      'https://tickets.interpark.com/contents/_next/image?url=https%3A%2F%2Fticketimage.interpark.com%2FPlay%2Fimage%2Flarge%2F23%2F23010643_p.gif&w=1200&q=75',
    title: '노엘 갤러거 하이 플라잉 버즈',
    location: '잠실 실내 체육관',
    date: '2023-09-11 ~ 2023-09-12',
    concertdetail:
      'https://ticketimage.interpark.com/Play/image/etc/23/23010643-05.jpg',
    time: '20: 00 ~ 22: 00',
  },
  {
    id: 6,
    image:
      'https://tickets.interpark.com/contents/_next/image?url=https%3A%2F%2Fticketimage.interpark.com%2FPlay%2Fimage%2Flarge%2FP0%2FP0003604_p.gif&w=750&q=75',
    title: 'LOVE IN SEOUL 라라랜드 인 콘서트',
    location: '세종문화회관 대극장',
    date: '2023-09-11 ~ 2023-09-12',
    concertdetail:
      'https://ticketimage.interpark.com/Play/image/etc/23/P0003604-02.jpg',
    time: '20: 00 ~ 22: 00',
  },
  {
    id: 7,
    image:
      'https://tickets.interpark.com/contents/_next/image?url=https%3A%2F%2Fticketimage.interpark.com%2FPlay%2Fimage%2Flarge%2F23%2F23011405_p.gif&w=750&q=75',
    title: '경기인디뮤직패스티벌 2023',
    location: '인산 와~ 스타디움',
    date: '2023-09-11 ~ 2023-09-12',
    concertdetail:
      'https://ticketimage.interpark.com/Play/image/etc/23/23011405-07.jpg',
    time: '20: 00 ~ 22: 00',
  },
  {
    id: 8,
    image:
      'https://tickets.interpark.com/contents/_next/image?url=https%3A%2F%2Fticketimage.interpark.com%2FPlay%2Fimage%2Flarge%2F23%2F23012677_p.gif&w=750&q=75',
    title: '쇼킹나이트 전국투어 콘서트',
    location: '올림픽공원 올림픽 홀',
    date: '2023-09-11 ~ 2023-09-12',
    concertdetail:
      'https://ticketimage.interpark.com/Play/image/etc/23/23012677-05.jpg',
    time: '20: 00 ~ 22: 00',
  },

  {
    id: 9,
    image:
      'https://tickets.interpark.com/contents/_next/image?url=https%3A%2F%2Fticketimage.interpark.com%2FPlay%2Fimage%2Flarge%2F23%2F23012698_p.gif&w=1200&q=75',
    title: '임영웅 콘서트 IM HERO TOUR 2023 - 서울',
    location: 'KSPO DOME(올림픽체조경기장)',
    date: '2023-09-18 ~ 2023-09-15',
    concertdetail:
      'https://ticketimage.interpark.com/Play/image/etc/23/23012698-06.jpg',
    time: '20: 00 ~ 22: 00',
  },
  {
    id: 10,
    image:
      'https://tickets.interpark.com/contents/_next/image?url=https%3A%2F%2Fticketimage.interpark.com%2FPlay%2Fimage%2Flarge%2F23%2F23012636_p.gif&w=750&q=75',
    title: 'AKMU 2023 CONCERT[AKMUTOPIA]',
    concertdetail:
      'https://ticketimage.interpark.com/Play/image/etc/23/23012698-06.jpg',
    location: '경희대학교 평화의전당',
    date: '2023-09-09 ~ 2023-09-11',
    time: '20: 00 ~ 22: 00',
  },
];

export default dummyConcerts;
