// api.js
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import { message } from 'antd';
import { useNavigate } from 'react-router-dom';

// Создаем экземпляр Axios
const api = axios.create({
    baseURL: 'http://localhost:8080', // URL вашего сервера
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
            const currentTime = Date.now() / 1000; // текущее время в секундах

            if (decodedToken.exp < currentTime) {
                // Если токен истек
                localStorage.removeItem('token'); // Удаляем токен
                message.warning('Your session has expired, please log in again.');
                window.location.href = '/login'; // Перенаправляем на страницу логина
                return Promise.reject(new Error('Token expired'));
            }

            // Если токен валиден, добавляем его в заголовок
            config.headers['Authorization'] = `Bearer ${token}`;
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Интерсептор для обработки ошибок
api.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        if (error.response && error.response.status === 401) {
            // Если сервер вернул 401, это может означать, что токен истек или неверный
            localStorage.removeItem('token');
            message.error('Unauthorized, please log in.');
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

export default api;
