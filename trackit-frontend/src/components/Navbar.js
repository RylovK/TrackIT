import React from 'react';
import { Button, Divider } from 'antd';
import { useNavigate } from 'react-router-dom';

const Navbar = () => {
    const navigate = useNavigate();

    const handleMainPage = () => {
        navigate('/'); // Path to the main page
    };

    const handleEquipmentList = () => {
        navigate('/equipment'); //
    };

    const handleCreateEquipment = () => {
        navigate('/equipment/create'); //
    };

    const handleCreatePartNumber = () => {
        navigate('/partnumber/create'); // Path for PartNumberEditPage
    };

    const handlePartNumberList = () => {
        navigate('/partnumber/all'); // Path for PartNumberEditPage
    };

    const handleCreateJob = () => {
        navigate('/job/create'); // Path for JobEditPage
    };

    const handleJobListPage = () => {
        navigate('/job'); // Path for Job list page
    };

    const handleCertificationPage = () => {
        navigate('/certified-equipment'); // Path for CertifiedEquipmentPage
    };

    return (
        <div style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'flex-start',
            padding: '20px',
            width: '200px', // Width of the sidebar
            backgroundColor: '#f0f2f5', // Background color
            height: '100vh', // Height of the panel to full page
        }}>
            <Button type="primary" onClick={handleMainPage} style={{ marginBottom: '10px' }}>
                Main
            </Button>

            <Divider style={{ margin: '10px 0' }} /> {/* Divider for grouping */}

            <Button type="primary" onClick={handleEquipmentList} style={{ marginBottom: '10px' }}>
                All equipment list
            </Button>

            <Button type="primary" onClick={handleCreateEquipment} style={{ marginBottom: '10px' }}>
                Create Equipment
            </Button>



            <Divider style={{ margin: '10px 0' }} /> {/* Divider for grouping */}

            <Button type="primary" onClick={handleCreateJob} style={{ marginBottom: '10px' }}>
                Create Job
            </Button>
            <Button type="primary" onClick={handleJobListPage} style={{ marginBottom: '10px' }}>
                Job List
            </Button>

            <Divider style={{ margin: '10px 0' }} /> {/* Optional: another divider for the next group */}

            <Button type="primary" onClick={handleCertificationPage} style={{ marginBottom: '10px' }}>
                Certification Page
            </Button>

            <Divider style={{ margin: '10px 0' }} /> {/* Divider for grouping */}
            <Divider style={{ margin: '10px 0' }} /> {/* Divider for grouping */}

            <Button type="primary" onClick={handleCreatePartNumber} style={{ marginBottom: '10px' }}>
                Create Part Number
            </Button>

            <Button type="primary" onClick={handlePartNumberList} style={{ marginBottom: '10px' }}>
                List of all part numbers
            </Button>
        </div>
    );
};

export default Navbar;
