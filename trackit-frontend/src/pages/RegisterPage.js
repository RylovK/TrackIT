import React, { useState } from 'react';
import { Form, Input, Button, message } from 'antd';

const RegisterPage = () => {
    const [errors, setErrors] = useState({}); // State to hold validation errors

    const handleRegister = async (values) => {
        try {
            const response = await fetch('http://localhost:8080/auth/register', {
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
                throw new Error('Registration failed');
            }

            // Registration successful
            message.success('User registered successfully');
            setErrors({}); // Clear errors if registration is successful
        } catch (error) {
            console.error('Error during registration:', error);
            // Optionally, display a generic error message
            message.error('Something went wrong. Please try again.');
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
