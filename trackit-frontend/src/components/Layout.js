import React from 'react';
import { Outlet } from 'react-router-dom';
import Navbar from './Navbar';

const Layout = () => {
    return (
        <div style={{ display: 'flex', height: '100vh' }}>
            <Navbar />
            <div style={{ flex: 1, padding: '20px', overflowY: 'auto' }}>
                <Outlet />
            </div>
        </div>
    );
};

export default Layout;
