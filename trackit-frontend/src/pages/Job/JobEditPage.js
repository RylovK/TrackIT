import React, { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom'; // Import Link
import axios from 'axios';
import { Button, Spin, message, Input, Table } from 'antd';

const JobEditPage = () => {
    const { id } = useParams(); // Get the job ID from the URL
    const [job, setJob] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isEditing, setIsEditing] = useState(false); // State to toggle edit mode
    const [jobName, setJobName] = useState(''); // State for job name input

    const fetchJob = async () => {
        const token = localStorage.getItem('token');
        try {
            const response = await axios.get(`http://localhost:8080/job/${id}`, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            setJob(response.data);
            setJobName(response.data.jobName); // Set jobName for editing
        } catch (err) {
            setError('Failed to fetch job');
            message.error('Failed to fetch job');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleEdit = () => {
        setIsEditing(true); // Switch to edit mode
    };

    const handleSave = async () => {
        const token = localStorage.getItem('token');
        try {
            const response = await axios.patch(`http://localhost:8080/job/${id}`, {
                id: job.id,
                jobName: jobName,
            }, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            setJob(response.data); // Update the job state with the new data
            setIsEditing(false); // Switch back to view mode
            message.success('Job updated successfully!');
        } catch (err) {
            setError('Failed to update job');
            message.error('Failed to update job');
            console.error(err);
        }
    };

    useEffect(() => {
        fetchJob();
    }, [id]);

    if (loading) return <Spin tip="Loading..." />;
    if (error) return <div>Error: {error}</div>;

    const equipmentColumns = [
        {
            title: 'Part Number',
            dataIndex: 'partNumber',
            key: 'partNumber',
            render: (text, record) => (
                <Link to={`/equipment/${record.id}`}>{text || 'N/A'}</Link> // Create a link to EquipmentEditPage
            ),
        },
        {
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
            render: (text) => text || 'N/A', // Display description if available
        },
        {
            title: 'Serial Number',
            dataIndex: 'serialNumber',
            key: 'serialNumber',
        },
    ];

    return (
        <div>
            <h1>{isEditing ? 'Edit Job' : 'Job Details'}</h1>
            {job && (
                <div>
                    {isEditing ? (
                        <div>
                            <Input
                                value={jobName}
                                onChange={(e) => setJobName(e.target.value)}
                                placeholder="Job Name"
                                maxLength={25} // Add max length as per your validation
                            />
                            <Button type="primary" onClick={handleSave} style={{ marginLeft: '10px' }}>
                                Save
                            </Button>
                        </div>
                    ) : (
                        <div>
                            <h2>Job Name: {job.jobName}</h2>
                            <Button type="default" onClick={handleEdit}>
                                Edit
                            </Button>
                        </div>
                    )}
                    <h3>Associated Equipment</h3>
                    {job.equipment && job.equipment.length > 0 ? (
                        <Table dataSource={Array.from(job.equipment)} columns={equipmentColumns} rowKey="id" />
                    ) : (
                        <p>No associated equipment found.</p>
                    )}
                </div>
            )}
        </div>
    );
};

export default JobEditPage;
