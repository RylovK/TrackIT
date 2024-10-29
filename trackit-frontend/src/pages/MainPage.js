import React, { useState } from 'react';
import { Input, Button, message } from 'antd';
import { useNavigate } from 'react-router-dom';
import api from '../api'; // Импортируем наш настроенный Axios

const MainPage = () => {
    const [serialNumber, setSerialNumber] = useState('');
    const navigate = useNavigate();

    const handleSearch = async () => {
        if (!serialNumber) {
            message.warning('Please enter a serial number!');
            return;
        }

        try {
            // Используем глобально настроенный Axios для запроса
            const response = await api.get('/equipment', {
                params: { serialNumber }, // Передаем фильтр как параметр
            });

            navigate('/equipment', { state: { filters: { serialNumber } } }); // Передаем только фильтры
        } catch (error) {
            console.error('Error during search:', error);
            message.error(error.message || 'Something went wrong while searching for the equipment.');
        }
    };

    const handleLogout = async () => {
        try {
            await api.post('/logout'); // Запрос на логаут к API
            localStorage.removeItem('token')
            message.success('You have been logged out');
            navigate('/login'); // Перенаправление на страницу входа
        } catch (error) {
            console.error('Error during logout:', error);
            message.error(error.message || 'Failed to logout.');
        }
    };

    return (
        <div style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'center',
            minHeight: '100vh',
            padding: '0 20px',
            boxSizing: 'border-box',
        }}>
            <Input
                placeholder="Enter Serial Number"
                value={serialNumber}
                onChange={(e) => setSerialNumber(e.target.value)}
                style={{
                    width: '400px', // Increased width for better visibility
                    fontSize: '16px',
                    height: '40px',
                    marginBottom: '20px',
                    textAlign: 'center',
                }}
            />
            <Button type="primary" onClick={handleSearch} style={{ fontSize: '16px', marginBottom: '20px' }}>
                Find
            </Button>
            <Button type="default" onClick={handleLogout} style={{ fontSize: '16px' }}>
                Logout
            </Button>
        </div>
    );
};

export default MainPage;
