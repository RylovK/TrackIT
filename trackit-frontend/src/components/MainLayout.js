import React from 'react';
import Navbar from './Navbar'; // Импортируем Navbar
import { Outlet } from 'react-router-dom'; // Используется для рендеринга вложенных маршрутов

const MainLayout = () => {
    return (
        <div style={{
            display: 'flex',
            height: '100vh', // Макет занимает всю высоту окна
        }}>
            <Navbar /> {/* Панель навигации слева */}
            <div style={{
                flex: 1, // Основной контент занимает оставшееся пространство
                padding: '20px', // Добавляем отступы для контента
                overflowY: 'auto', // Скроллинг для основного контента, если он большой
            }}>
                <Outlet /> {/* Контент страниц будет рендериться здесь */}
            </div>
        </div>
    );
};

export default MainLayout;
