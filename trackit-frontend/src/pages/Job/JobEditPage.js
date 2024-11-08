import React, { useEffect, useState, useCallback } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom'; // Import useNavigate
import api from '../../api'; // Import your axios instance
import { Button, Spin, message, Input, Table } from 'antd';

const JobEditPage = () => {
    const { id } = useParams(); // Get the job ID from the URL
    const navigate = useNavigate(); // Initialize useNavigate for redirecting
    const [job, setJob] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isEditing, setIsEditing] = useState(false); // State to toggle edit mode
    const [jobName, setJobName] = useState(''); // State for job name input

    const fetchJob = useCallback(async () => {
        setLoading(true); // Set loading to true at the beginning of the fetch
        try {
            const response = await api.get(`/job/${id}`); // Use your axios instance
            setJob(response.data);
            setJobName(response.data.jobName); // Set jobName for editing
        } catch (err) {
            setError('Failed to fetch job');
            message.error('Failed to fetch job');
            console.error(err);
        } finally {
            setLoading(false);
        }
    }, [id]);

    const handleEdit = () => {
        setIsEditing(true); // Switch to edit mode
    };

    const handleSave = async () => {
        try {
            const response = await api.patch(`/job/${id}`, {
                id: job.id,
                jobName: jobName,
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

    const handleDelete = async () => {
        try {
            await api.delete(`/job/${id}`); // Send delete request
            message.success('Job deleted successfully!');
            navigate('/job'); // Redirect to the job list page after deletion
        } catch (err) {
            setError('Failed to delete job');
            message.error('Failed to delete job');
            console.error(err);
        }
    };

    useEffect(() => {
        fetchJob();
    }, [fetchJob]);

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
                            <Button
                                type="danger"
                                onClick={handleDelete}
                                style={{ marginLeft: '10px' }}
                            >
                                Delete
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
