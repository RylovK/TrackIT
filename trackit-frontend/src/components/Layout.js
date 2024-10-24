import React from 'react';
import { Outlet } from 'react-router-dom';
import Navbar from './Navbar'; // Подключаем Navbar

const Layout = () => {
    return (
        <div style={{ display: 'flex', height: '100vh' }}>
            <Navbar /> {/* Панель навигации слева */}
            <div style={{ flex: 1, padding: '20px', overflowY: 'auto' }}>
                <Outlet /> {/* Место для рендеринга страниц */}
            </div>
        </div>
    );
};

export default Layout;
