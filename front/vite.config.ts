import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  optimizeDeps: {
    exclude: ['lucide-react'],
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8081',
        // target: 'http://127.0.0.1:4523/m1/6381204-6194941-default',
        changeOrigin: true,
        secure: false,
      }
    }
  }
});
