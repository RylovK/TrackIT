import React, { useState } from 'react';
import { Form, Input, Button, message } from 'antd';
import { useNavigate } from 'react-router-dom'; // Импортируем useNavigate
import api from '../../api';

const PartNumberCreatingPage = () => {
    const [form] = Form.useForm();
    const [validationErrors, setValidationErrors] = useState({});
    const navigate = useNavigate(); // Инициализируем navigate

    const onFinish = async (values) => {
        try {
            // Очищаем предыдущие ошибки
            setValidationErrors({});

            // Отправляем запрос на сервер
            const response = await api.post('/partNumber', values);

            // Проверяем статус ответа
            if (response.status === 201) {
                message.success('Part number created successfully');

                // Редирект на страницу информации о новом номере детали
                navigate(`/partnumber/${response.data.number}`);
            }

        } catch (error) {
            if (error.response) {
                if (error.response.status === 400 && error.response.data) {
                    // Установка ошибок валидации полей
                    setValidationErrors(error.response.data);
                } else if (error.response.status === 403) {
                    // Ошибка доступа
                    message.error(error.response.data);
                } else {
                    message.error('An unexpected error occurred');
                }
            }
        }
    };

    return (
        <div>
            <h1>Create New Part Number</h1>
            <Form form={form} onFinish={onFinish} layout="vertical">
                <Form.Item
                    label="Number"
                    name="number"
                    validateStatus={validationErrors.number ? 'error' : ''}
                    help={validationErrors.number}
                >
                    <Input />
                </Form.Item>

                <Form.Item
                    label="Description"
                    name="description"
                    validateStatus={validationErrors.description ? 'error' : ''}
                    help={validationErrors.description}
                >
                    <Input />
                </Form.Item>

                <Button type="primary" htmlType="submit">
                    Save
                </Button>
            </Form>
        </div>
    );
};

export default PartNumberCreatingPage;
