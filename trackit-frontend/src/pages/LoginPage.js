import React, { useState } from 'react';
import { Form, Input, Button, message } from 'antd';
import { useNavigate } from 'react-router-dom';
import api from '../api'; // Импортируем экземпляр axios

const LoginPage = () => {
    const [errors, setErrors] = useState({});
    const navigate = useNavigate();

    const handleLogin = async (values) => {
        try {
            const response = await api.post('/auth/login', values); // Используем api для запроса

            const { token } = response.data;
            // Сохраняем токен в localStorage
            localStorage.setItem('token', token);
            message.success('Login successful');
            // Перенаправление на главную страницу или защищенный маршрут
            navigate('/main'); // Настройте путь в зависимости от структуры вашего приложения
        } catch (error) {
            if (error.response && error.response.status === 401) {
                // Получаем ошибки из ответа и устанавливаем их в состояние
                //setErrors(error.response.data);
                message.error('Invalid username or password');
            } else {
                console.error('Error during login:', error);
                message.error('Something went wrong. Please try again.');
            }
        }
    };

    return (
        <div style={{ maxWidth: 400, margin: '0 auto' }}>
            <h2>Login</h2>
            <Form onFinish={handleLogin} layout="vertical">
                <Form.Item
                    label="Username"
                    name="username"
                    validateStatus={errors.username ? 'error' : ''}
                    help={errors.username}
                    rules={[
                        { required: true, message: 'Please input your username!' },
                        { min: 4, max: 20, message: 'The username must be between 4 and 20 symbols' },
                    ]}
                >
                    <Input />
                </Form.Item>

                <Form.Item
                    label="Password"
                    name="password"
                    validateStatus={errors.password ? 'error' : ''}
                    help={errors.password}
                    rules={[
                        { required: true, message: 'Please input your password!' },
                        { min: 4, message: 'The password cannot be empty' },
                    ]}
                >
                    <Input.Password />
                </Form.Item>

                <Form.Item>
                    <Button type="primary" htmlType="submit" block>
                        Login
                    </Button>
                </Form.Item>
            </Form>
        </div>
    );
};

export default LoginPage;
