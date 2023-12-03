import { atom } from 'recoil';

const hamburgerState = atom<boolean>({
  key: 'hamburgerState',
  default: false,
});

export default hamburgerState;
