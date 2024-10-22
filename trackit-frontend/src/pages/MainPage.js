import React, { useState } from 'react';
import { Input, Button, message } from 'antd';
import { useNavigate } from 'react-router-dom';

const MainPage = () => {
    const [serialNumber, setSerialNumber] = useState('');
    const navigate = useNavigate();

    const handleSearch = async () => {
        if (!serialNumber) {
            message.warning('Please enter a serial number!');
            return;
        }

        const token = localStorage.getItem('token'); // Retrieve the token from local storage
        const url = `http://localhost:8080/equipment?serialNumber=${serialNumber}`;
        console.log('Fetching from URL:', url);

        try {
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`, // Use the token here
                    'Content-Type': 'application/json',
                },
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error fetching equipment');
            }

            const data = await response.json();
            navigate('/equipment', { state: { filters: { serialNumber } } }); // Передаем только фильтры
        } catch (error) {
            console.error('Error during search:', error);
            message.error(error.message || 'Something went wrong while searching for the equipment.');
        }
    };

    return (
        <div style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'center',
            height: 'calc(100vh - 256px)', // Adjust based on navbar height
            marginTop: '16px',
            paddingLeft: '200px', // Add padding to move everything right
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
                }}
            />
            <Button type="primary" onClick={handleSearch} style={{ fontSize: '16px' }}>
                Find
            </Button>
        </div>
    );
};

export default MainPage;
