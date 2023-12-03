// // src/global.d.ts
// declare global {
//   interface Window {
//     ethereum?: {
//       request: (args: { method: string; params?: any[] }) => Promise<any>;
//     };
//   }
// }

// // 이제 다른 TypeScript 파일에서 window.ethereum을 사용해도 에러가 발생하지 않습니다.
