// PartNumberList.jsx
import React, { useEffect, useState } from 'react';
import { Table } from 'antd';
import { Link } from 'react-router-dom';
import api from '../../api'; // Импортируем экземпляр api

const PartNumberList = () => {
    const [partNumbers, setPartNumbers] = useState([]);

    useEffect(() => {
        const fetchPartNumbers = async () => {
            try {
                const response = await api.get('/partNumber'); // Используем экземпляр api
                setPartNumbers(response.data);
            } catch (error) {
                console.error('Ошибка при загрузке данных:', error);
            }
        };

        fetchPartNumbers();
    }, []);

    const columns = [
        {
            title: 'Part Number',
            dataIndex: 'number',
            render: (text, record) => (
                <Link to={`/partnumber/${record.number}`}>{text}</Link>
            ),
        },
        {
            title: 'Description',
            dataIndex: 'description',
        },
    ];

    return (
        <Table
            dataSource={partNumbers}
            columns={columns}
            rowKey="number"
        />
    );
};

export default PartNumberList;
