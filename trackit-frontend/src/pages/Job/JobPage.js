import React, { useEffect, useState } from 'react';
import api from '../../api'; // Import your axios instance
import { Table, Spin, message } from 'antd';
import { Link } from 'react-router-dom';

const { Column } = Table;

const JobPage = () => {
    const [jobs, setJobs] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchJobs = async () => {
        try {
            const response = await api.get('/job'); // Use your axios instance
            console.log('API Response:', response.data);
            setJobs(response.data); // Ensure this is an array
        } catch (err) {
            setError('Failed to fetch jobs');
            message.error('Failed to fetch jobs');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchJobs();
    }, []);

    if (loading) return <Spin tip="Loading..." />;

    if (error) return <div>Error: {error}</div>;

    return (
        <div>
            <h1>Job List</h1>
            <Table dataSource={jobs} rowKey="id">
                <Column
                    title="Job Name"
                    dataIndex="jobName"
                    key="jobName"
                    render={(text, record) => (
                        <Link to={`/job/${record.id}`}>{text}</Link> // Make job names clickable links
                    )}
                />
                {/* Add more columns as needed */}
            </Table>
        </div>
    );
};

export default JobPage;
