import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import { message } from 'antd';

// Создаем экземпляр Axios
const api = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json',
    },
});

// Интерсептор для проверки токена перед каждым запросом
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            const decodedToken = jwtDecode(token);
            const currentTime = Date.now() / 1000;

            if (decodedToken.exp < currentTime) {
                localStorage.removeItem('token');
                message.warning('Your session has expired, please log in again.');
                window.location.href = '/login';
                return Promise.reject(new Error('Token expired'));
            }

            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// Интерсептор для обработки ошибок
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response) {
            // Обработка различных кодов ошибок
            if (error.response.status === 401) {
                localStorage.removeItem('token');
                message.error('Invalid username or password', 3); // Показать сообщение на 3 секунды
            } else {
                message.error('Something went wrong. Please try again.', 3);
            }
        }
        return Promise.reject(error);
    }
);

export default api;
