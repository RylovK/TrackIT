import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { Button, Input, message } from 'antd';

const JobCreatePage = () => {
    const [jobName, setJobName] = useState(''); // State for job name input
    const [loading, setLoading] = useState(false); // Loading state for API call
    const navigate = useNavigate(); // For navigation after creation

    const handleSubmit = async (e) => {
        e.preventDefault(); // Prevent default form submission
        setLoading(true); // Set loading state

        const token = localStorage.getItem('token'); // Retrieve JWT token
        try {
            // Make POST request to create a new job
            const response = await axios.post('http://localhost:8080/job', {
                jobName: jobName,
            }, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });

            // Handle successful job creation
            message.success('Job created successfully!');
            navigate(`/job/${response.data.id}`); // Redirect to the created job's page
        } catch (err) {
            // Handle errors
            if (err.response && err.response.data) {
                const errors = err.response.data.errors;
                if (errors) {
                    errors.forEach(error => message.error(error.defaultMessage));
                } else {
                    message.error('Failed to create job');
                }
            } else {
                message.error('Failed to create job');
            }
        } finally {
            setLoading(false); // Reset loading state
        }
    };

    return (
        <div>
            <h1>Create Job</h1>
            <form onSubmit={handleSubmit}>
                <div>
                    <Input
                        value={jobName}
                        onChange={(e) => setJobName(e.target.value)}
                        placeholder="Job Name"
                        maxLength={25} // Max length as per your validation
                        required
                    />
                </div>
                <div style={{ marginTop: '10px' }}>
                    <Button type="primary" htmlType="submit" loading={loading}>
                        Create Job
                    </Button>
                </div>
            </form>
        </div>
    );
};

export default JobCreatePage;
