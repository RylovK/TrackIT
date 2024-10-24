import React, { useState, useEffect } from 'react';
import { Table, Input, Button, message, Select } from 'antd';
import { useLocation, useNavigate } from 'react-router-dom';

const { Option } = Select;

const EquipmentPage = () => {
    const [equipmentData, setEquipmentData] = useState([]);
    const [jobNames, setJobNames] = useState([]); // Хранение уникальных jobName
    const [filters, setFilters] = useState({
        serialNumber: '',
        partNumber: '',
        healthStatus: '',
        allocationStatus: '',
        jobName: '',
    });

    const location = useLocation();
    const navigate = useNavigate(); // Используем хук для навигации

    useEffect(() => {
        if (location.state && location.state.filters) {
            const initialFilters = location.state.filters;
            setFilters((prev) => ({
                ...prev,
                serialNumber: initialFilters.serialNumber || '',
            }));
            fetchData(initialFilters);
        } else {
            fetchData();
        }
    }, [location.state]);

    const fetchData = async (filterParams = {}) => {
        const token = localStorage.getItem('token');

        const filteredParams = Object.fromEntries(
            Object.entries(filterParams).filter(([_, value]) => value)
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
            extractJobNames(data.content); // Извлекаем уникальные jobName
        } catch (error) {
            console.error('Error fetching equipment:', error);
            message.error(error.message || 'Something went wrong while fetching equipment.');
        }
    };

    const extractJobNames = (data) => {
        const uniqueJobNames = [...new Set(data.map(item => item.jobName).filter(Boolean))];
        setJobNames(uniqueJobNames);
    };

    const handleFilterChange = (e) => {
        const { name, value } = e.target;
        setFilters({ ...filters, [name]: value });
    };

    const handleHealthStatusChange = (value) => {
        setFilters({ ...filters, healthStatus: value });
    };

    const handleAllocationStatusChange = (value) => {
        setFilters({ ...filters, allocationStatus: value });
    };

    const handleJobNameChange = (value) => {
        setFilters({ ...filters, jobName: value });
    };

    const applyFilters = () => {
        fetchData(filters);
    };

    const resetFilters = () => {
        setFilters({
            serialNumber: '',
            partNumber: '',
            healthStatus: '',
            allocationStatus: '',
            jobName: '',
        });
        fetchData();
    };

    const handleRowClick = (id) => {
        navigate(`/equipment/${id}`); // Навигация к EquipmentEditPage с id
    };

    const exportAllEquipment = async () => {
        const token = localStorage.getItem('token');
        try {
            const response = await fetch('http://localhost:8080/export/all', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            const blob = await response.blob();
            const url = window.URL.createObjectURL(new Blob([blob]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'equipment.xlsx');
            document.body.appendChild(link);
            link.click();
            link.parentNode.removeChild(link);
        } catch (error) {
            console.error('Error exporting equipment:', error);
            message.error('Error exporting all equipment.');
        }
    };

    const exportCertifiedEquipment = async () => {
        const token = localStorage.getItem('token');
        try {
            const response = await fetch('http://localhost:8080/export/certified', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            const blob = await response.blob();
            const url = window.URL.createObjectURL(new Blob([blob]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'certified_equipment.xlsx');
            document.body.appendChild(link);
            link.click();
            link.parentNode.removeChild(link);
        } catch (error) {
            console.error('Error exporting certified equipment:', error);
            message.error('Error exporting certified equipment.');
        }
    };

    const columns = [
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
            <div style={{ marginBottom: '20px' }}>
                <div style={{ display: 'flex', marginBottom: '5px' }}>
                    <div style={{ width: '150px' }}>Part Number</div>
                    <div style={{ width: '150px' }}>Serial Number</div>
                    <div style={{ width: '150px' }}>Health Status</div>
                    <div style={{ width: '150px' }}>Allocation Status</div>
                    <div style={{ width: '150px' }}>Job Name</div>
                </div>
                <div style={{ display: 'flex', marginBottom: '20px' }}>
                    <Input
                        placeholder="Part Number"
                        name="partNumber"
                        value={filters.partNumber}
                        onChange={handleFilterChange}
                        style={{ marginRight: '10px', width: '150px' }}
                    />
                    <Input
                        placeholder="Serial Number"
                        name="serialNumber"
                        value={filters.serialNumber}
                        onChange={handleFilterChange}
                        style={{ marginRight: '10px', width: '150px' }}
                    />
                    <Select
                        placeholder="Health Status"
                        name="healthStatus"
                        value={filters.healthStatus}
                        onChange={handleHealthStatusChange}
                        style={{ marginRight: '10px', width: '150px' }}
                    >
                        <Option value="">All</Option>
                        <Option value="RITE">RITE</Option>
                        <Option value="FXD">FXD</Option>
                        <Option value="RONG">RONG</Option>
                        <Option value="JUNKED">JUNKED</Option>
                    </Select>
                    <Select
                        placeholder="Allocation Status"
                        name="allocationStatus"
                        value={filters.allocationStatus}
                        onChange={handleAllocationStatusChange}
                        style={{ marginRight: '10px', width: '150px' }}
                    >
                        <Option value="">All</Option>
                        <Option value="ON_LOCATION">ON_LOCATION</Option>
                        <Option value="ON_BASE">ON_BASE</Option>
                    </Select>
                    <Select
                        placeholder="Job Name"
                        name="jobName"
                        value={filters.jobName}
                        onChange={handleJobNameChange}
                        style={{ marginRight: '10px', width: '150px' }}
                    >
                        <Option value="">All</Option>
                        {jobNames.map((name, index) => (
                            <Option key={index} value={name}>{name}</Option>
                        ))}
                    </Select>
                </div>
                <div style={{ display: 'flex', marginBottom: '20px' }}>
                    <Button type="primary" onClick={applyFilters} style={{ marginRight: '10px' }}>
                        Apply Filters
                    </Button>
                    <Button onClick={resetFilters} style={{ marginRight: '10px' }}>
                        Reset Filters
                    </Button>
                </div>
            </div>

            <Table
                dataSource={equipmentData}
                columns={columns}
                rowKey="id"
                pagination={false}
                onRow={(record) => ({
                    onClick: () => handleRowClick(record.id),
                })}
            />

            <div style={{ marginTop: '20px', display: 'flex', justifyContent: 'space-between' }}>
                <Button type="primary" onClick={exportAllEquipment}>
                    Export all equipment
                </Button>
                <Button type="primary" onClick={exportCertifiedEquipment}>
                    Export certified equipment
                </Button>
            </div>
        </div>
    );
};

export default EquipmentPage;
