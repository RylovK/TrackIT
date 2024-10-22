// src/components/Navbar.js
import React from 'react';
import { Button } from 'antd';
import { useNavigate } from 'react-router-dom';

const Navbar = () => {
    const navigate = useNavigate();

    const handleMainPage = () => {
        navigate('/'); // Путь к главной странице
    };

    const handleCreateEquipment = () => {
        navigate('/equipment/edit'); // Укажите правильный путь для EquipmentEditPage
    };

    const handleCreatePartNumber = () => {
        navigate('/partnumber/edit'); // Укажите правильный путь для PartNumberEditPage
    };

    const handleCreateJob = () => {
        navigate('/job/edit'); // Укажите правильный путь для JobEditPage
    };

    const handleCertificationPage = () => {
        navigate('/certified-equipment'); // Укажите правильный путь для CertifiedEquipmentPage
    };

    return (
        <div style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'flex-start',
            padding: '20px',
            width: '200px', // Ширина боковой панели
            backgroundColor: '#f0f2f5', // Цвет фона
            height: '100vh', // Высота панели на всю страницу
        }}>
            <Button type="primary" onClick={handleMainPage} style={{ marginBottom: '10px' }}>
                Main
            </Button>
            <Button type="primary" onClick={handleCreateEquipment} style={{ marginBottom: '10px' }}>
                Create Equipment
            </Button>
            <Button type="primary" onClick={handleCreatePartNumber} style={{ marginBottom: '10px' }}>
                Create Part Number
            </Button>
            <Button type="primary" onClick={handleCreateJob} style={{ marginBottom: '10px' }}>
                Create Job
            </Button>
            <Button type="primary" onClick={handleCertificationPage} style={{ marginBottom: '10px' }}>
                Certification Page
            </Button>
        </div>
    );
};

export default Navbar;
