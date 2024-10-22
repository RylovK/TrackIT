import React, { useState } from 'react';
import { Form, Input, Button, message } from 'antd';
import { useNavigate } from 'react-router-dom'; // Import useNavigate for navigation

const LoginPage = () => {
    const [errors, setErrors] = useState({});
    const navigate = useNavigate(); // Use useNavigate instead of useHistory

    const handleLogin = async (values) => {
        try {
            const response = await fetch('http://localhost:8080/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(values),
            });

            if (!response.ok) {
                const errorData = await response.json();
                // Set the errors state with the response errors
                setErrors(errorData);
                throw new Error('Login failed');
            }

            const data = await response.json();
            // Store the token in local storage
            localStorage.setItem('token', data.token);
            message.success('Login successful');
            // Redirect to the main page or any protected route
            navigate('/main'); // Adjust this according to your app structure
        } catch (error) {
            console.error('Error during login:', error);
            // Optionally, display a generic error message
            message.error('Something went wrong. Please try again.');
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
