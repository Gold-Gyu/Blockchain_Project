import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react-swc';
import tsconfigPaths from 'vite-tsconfig-paths';
import checker from 'vite-plugin-checker';
import * as path from 'node:path';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    react(),
    tsconfigPaths(),
    checker({
      typescript: true,
    }),
  ],
  server: {
    proxy: {
      '/ws': {
        target: 'wss://j9c203.p.ssafy.io/',
        changeOrigin: true,
        secure: false,
        ws: true,
      },
      '/api': {
        target: 'https://j9c203.p.ssafy.io/',
        changeOrigin: true,
        // secure: false,
        ws: true,
      },
    },
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
});
