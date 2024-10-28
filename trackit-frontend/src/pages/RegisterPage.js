import React, { useState } from 'react';
import { Form, Input, Button, message } from 'antd';
import api from '../api'; // Импортируем заранее настроенный экземпляр axios

const RegisterPage = () => {
    const [errors, setErrors] = useState({}); // Состояние для хранения ошибок валидации

    const handleRegister = async (values) => {
        try {
            await api.post('/auth/register', values); // Убираем переменную response

            message.success('User registered successfully');
            setErrors({}); // Очищаем ошибки, если регистрация успешна
        } catch (error) {
            if (error.response && error.response.data) {
                // Обновляем состояние errors данными из ответа
                setErrors(error.response.data);
            } else {
                console.error('Error during registration:', error);
                message.error('Something went wrong. Please try again.');
            }
        }
    };

    return (
        <div style={{ maxWidth: 400, margin: '0 auto' }}>
            <h2>Register</h2>
            <Form onFinish={handleRegister} layout="vertical">
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
                        Register
                    </Button>
                </Form.Item>
            </Form>
        </div>
    );
};

export default RegisterPage;
