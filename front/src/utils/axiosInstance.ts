// src/utils/axiosInstance.ts
import axios from 'axios';
import Cookies from "js-cookie";

const axiosInstance = axios.create({
    // baseURL: 'http://127.0.0.1:4523/m1/6381204-6194941-default', // 替换为你的后端 API 地址
    // baseURL: "http://localhost:8081", // 移除这行，使用代理
    baseURL: "", // 使用相对路径，让 Vite 代理处理
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
    },
});

// 你可以在这里添加请求拦截器和响应拦截器
axiosInstance.interceptors.request.use(
    config => {
        // 定义不需要token的路径
        const noTokenPaths = ['/auth/login', '/auth/register', '/merchants/auth/login', '/merchants/auth/register'];

        // 检查当前请求是否为不需要token的请求
        const needsToken = !noTokenPaths.some(path => config.url?.includes(path));

        if (needsToken) {
            const token = Cookies.get('auth_token');
            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
            }
        }else{
            console.log("No need token!");
        }

        return config;
    },
    error => Promise.reject(error)
);

axiosInstance.interceptors.response.use(
    response => response,
    error => {
        // 检查是否为 token 无效错误
        if (error.response?.status === 401 || error.response?.status === 403) {
            // 清空所有相关的 Cookies
            Cookies.remove('auth_token');
            Cookies.remove('merchant_auth_token');
            Cookies.remove('merchant_role_type');
            Cookies.remove('current_merchant_id');
            Cookies.remove('user_auth_token');
            Cookies.remove('current_user_id');

            // 导航到登录页
            // window.location.href = '/login';
            console.log("error while retrieving server");
            console.log(error);

        }

        return Promise.reject(error);
    }
);

export default axiosInstance;