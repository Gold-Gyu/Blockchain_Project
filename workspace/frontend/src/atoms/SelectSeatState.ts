import { atom } from 'recoil';

const SelectSeatState = atom<number[]>({
  key: 'SelectSeatState',
  default: [],
});

export default SelectSeatState;
