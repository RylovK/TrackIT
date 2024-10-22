import React, { useState, useEffect } from 'react';
import { Table, Input, Button, message } from 'antd';
import { useLocation } from 'react-router-dom';

const EquipmentPage = () => {
    const [equipmentData, setEquipmentData] = useState([]);
    const [filters, setFilters] = useState({
        serialNumber: '',
        partNumber: '',
        healthStatus: '',
        allocationStatus: '',
        jobName: '',
    });

    const location = useLocation();

    // Получаем фильтры из состояния, переданного с MainPage
    useEffect(() => {
        if (location.state && location.state.filters) {
            const initialFilters = location.state.filters;
            setFilters((prev) => ({
                ...prev,
                serialNumber: initialFilters.serialNumber || '', // Устанавливаем serialNumber из состояния
            }));
            fetchData(initialFilters); // Загружаем данные с фильтрами
        } else {
            fetchData(); // Если нет фильтров, загружаем все данные
        }
    }, [location.state]); // Следим за изменением состояния

    const fetchData = async (filterParams = {}) => {
        const token = localStorage.getItem('token');

        // Фильтрация: оставляем только непустые параметры
        const filteredParams = Object.fromEntries(
            Object.entries(filterParams).filter(([_, value]) => value) // Оставляем только те пары, где значение не пустое
        );

        const query = new URLSearchParams(filteredParams).toString();
        const url = `http://localhost:8080/equipment${query ? '?' + query : ''}`;

        try {
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error fetching equipment');
            }

            const data = await response.json();
            setEquipmentData(data.content);
        } catch (error) {
            console.error('Error fetching equipment:', error);
            message.error(error.message || 'Something went wrong while fetching equipment.');
        }
    };

    const handleFilterChange = (e) => {
        const { name, value } = e.target;
        setFilters({ ...filters, [name]: value });
    };

    const applyFilters = () => {
        fetchData(filters); // Передаем текущие фильтры
    };

    const resetFilters = () => {
        setFilters({
            serialNumber: '',
            partNumber: '',
            healthStatus: '',
            allocationStatus: '',
            jobName: '',
        });
        fetchData(); // Fetch data without filters
    };

    // Определение колонок для таблицы Ant Design
    const columns = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: 'Part Number',
            dataIndex: 'partNumber',
            key: 'partNumber',
            render: (partNumber) => partNumber ? partNumber : 'N/A',
        },
        {
            title: 'Serial Number',
            dataIndex: 'serialNumber',
            key: 'serialNumber',
        },
        {
            title: 'Health Status',
            dataIndex: 'healthStatus',
            key: 'healthStatus',
            render: (healthStatus) => healthStatus ? healthStatus : 'N/A',
        },
        {
            title: 'Allocation Status',
            dataIndex: 'allocationStatus',
            key: 'allocationStatus',
            render: (allocationStatus) => allocationStatus ? allocationStatus : 'N/A',
        },
        {
            title: 'Job',
            dataIndex: 'jobName',
            key: 'jobName',
        },
        {
            title: 'Comments',
            dataIndex: 'comments',
            key: 'comments',
        },
        {
            title: 'Last Job',
            dataIndex: 'lastJob',
            key: 'lastJob',
        },
        {
            title: 'Allocation Status Last Modified',
            dataIndex: 'allocationStatusLastModified',
            key: 'allocationStatusLastModified',
            render: (date) => date ? new Date(date).toLocaleString() : 'N/A',
        },
        {
            title: 'Created At',
            dataIndex: 'createdAt',
            key: 'createdAt',
            render: (date) => new Date(date).toLocaleString(),
        },
    ];

    return (
        <div>
            <div style={{ display: 'flex', marginBottom: '20px' }}>
                <Input
                    placeholder="Serial Number"
                    name="serialNumber"
                    value={filters.serialNumber}
                    onChange={handleFilterChange}
                    style={{ marginRight: '10px' }}
                />
                <Input
                    placeholder="Part Number"
                    name="partNumber"
                    value={filters.partNumber}
                    onChange={handleFilterChange}
                    style={{ marginRight: '10px' }}
                />
                <Input
                    placeholder="Health Status"
                    name="healthStatus"
                    value={filters.healthStatus}
                    onChange={handleFilterChange}
                    style={{ marginRight: '10px' }}
                />
                <Input
                    placeholder="Allocation Status"
                    name="allocationStatus"
                    value={filters.allocationStatus}
                    onChange={handleFilterChange}
                    style={{ marginRight: '10px' }}
                />
                <Input
                    placeholder="Job Name"
                    name="jobName"
                    value={filters.jobName}
                    onChange={handleFilterChange}
                    style={{ marginRight: '10px' }}
                />
                <Button type="primary" onClick={applyFilters} style={{ marginRight: '10px' }}>
                    Apply Filters
                </Button>
                <Button onClick={resetFilters}>Reset Filters</Button>
            </div>
            <Table columns={columns} dataSource={equipmentData} rowKey="id" />
        </div>
    );
};

export default EquipmentPage;
