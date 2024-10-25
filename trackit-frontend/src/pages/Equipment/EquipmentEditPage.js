import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';

const EquipmentEditPage = () => {
    const { id } = useParams();
    const [equipment, setEquipment] = useState(null);
    const [isCertified, setIsCertified] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const [formData, setFormData] = useState({});
    const [validationErrors, setValidationErrors] = useState({});
    const [nonFieldErrors, setNonFieldErrors] = useState([]);
    const [partNumbers, setPartNumbers] = useState([]);
    const imageDirectory = 'http://localhost:8080/images/';
    const [jobs, setJobs] = useState([]);

    useEffect(() => {
        const token = localStorage.getItem('token');
        axios.get(`http://localhost:8080/equipment/${id}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        })
            .then(response => {
                setEquipment(response.data);
                setFormData(response.data);
                if (response.data.certificationStatus) {
                    setIsCertified(true);
                }
            })
            .catch(error => {
                console.error('Error fetching equipment:', error);
            });
    }, [id]);

    useEffect(() => {
        const token = localStorage.getItem('token');
        axios.get('http://localhost:8080/partNumber', {
            headers: { 'Authorization': `Bearer ${token}` }
        })
            .then(response => {
                setPartNumbers(response.data);
            })
            .catch(error => {
                console.error('Error fetching part numbers:', error);
            });
    }, []);

    useEffect(() => {
        const token = localStorage.getItem('token');
        axios.get('http://localhost:8080/job', {
            headers: { 'Authorization': `Bearer ${token}` }
        })
            .then(response => {
                setJobs(response.data);
            })
            .catch(error => {
                console.error('Error fetching jobs:', error);
            });
    }, []);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevFormData => ({ ...prevFormData, [name]: value }));
    };

    const handleSave = () => {
        const token = localStorage.getItem('token');
        const url = isCertified
            ? `http://localhost:8080/certified/${id}`
            : `http://localhost:8080/equipment/${id}`;

        axios.patch(url, formData, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                console.log("Equipment updated successfully:", response.data);
                setIsEditing(false);
                window.location.reload();
            })
            .catch(error => {
                if (error.response && error.response.status === 400) {
                    const validationErrors = error.response.data;
                    const fieldErrors = {};
                    const newNonFieldErrors = [];

                    for (let [key, message] of Object.entries(validationErrors)) {
                        if (formData.hasOwnProperty(key)) {
                            fieldErrors[key] = message;
                        } else {
                            newNonFieldErrors.push(message);
                        }
                    }

                    setValidationErrors(prevErrors => ({
                        ...prevErrors,
                        ...fieldErrors,
                    }));
                    setNonFieldErrors(newNonFieldErrors);
                } else {
                    console.error("Error updating equipment:", error);
                }
            });
    };

    if (!equipment) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <h1>Equipment Info</h1>
            <form>

                {formData.photo && (
                    <div>
                        <label>Image:</label>
                        <img
                            src={`${imageDirectory}${formData.photo}`}
                            alt="Equipment Photo"
                            style={{width: '200px', height: 'auto'}}
                        />
                    </div>
                )}

                <div>
                    <label>Serial Number:</label>
                    <input
                        type="text"
                        name="serialNumber"
                        value={formData.serialNumber || ''}
                        readOnly={!isEditing}
                        onChange={handleInputChange}
                    />
                    {validationErrors.serialNumber && (
                        <p className="error">{validationErrors.serialNumber}</p>
                    )}
                </div>
                <div>
                    <label>Part Number:</label>
                    <select
                        name="partNumber"
                        value={formData.partNumber || ''}
                        disabled={!isEditing}
                        onChange={handleInputChange}
                    >
                        {/*<option value="">Select Part Number</option>*/}
                        {partNumbers.map(part => (
                            <option key={part.id} value={part.number}>
                                {part.number}
                            </option>
                        ))}
                    </select>
                    {validationErrors.partNumber && (
                        <p className="error">{validationErrors.partNumber}</p>
                    )}
                </div>
                <div>
                    <label>Health Status:</label>
                    <select
                        name="healthStatus"
                        value={formData.healthStatus || ''}
                        disabled={!isEditing}
                        onChange={handleInputChange}
                    >
                        <option value="RITE">RITE</option>
                        <option value="FXD">FXD</option>
                        <option value="RONG">RONG</option>
                        <option value="JUNKED">JUNKED</option>
                    </select>
                </div>
                <div>
                    <label>Allocation Status:</label>
                    <select
                        name="allocationStatus"
                        value={formData.allocationStatus || ''}
                        disabled={!isEditing}
                        onChange={handleInputChange}
                    >
                        <option value="ON_LOCATION">ON_LOCATION</option>
                        <option value="ON_BASE">ON_BASE</option>
                    </select>
                </div>

                {formData.allocationStatus === "ON_LOCATION" && (
                    <div>
                        <label>Job:</label>
                        <select
                            name="jobName"
                            value={formData.jobName || ''}
                            disabled={!isEditing}
                            onChange={handleInputChange}
                        >
                            <option value="">Select Job</option>
                            {jobs.map(job => (
                                <option key={job.jobName} value={job.jobName}>
                                    {job.jobName}
                                </option>
                            ))}
                        </select>
                        {validationErrors.jobName && (
                            <p className="error">{validationErrors.jobName}</p>
                        )}
                    </div>
                )}


                <div>
                    <label>Comments:</label>
                    <input
                        type="text"
                        name="comments"
                        value={formData.comments || ''}
                        readOnly={!isEditing}
                        onChange={handleInputChange}
                    />
                </div>

                <div>
                    <label>Allocation Status Last Modified:</label>
                    <input
                        type="date"
                        name="allocationStatusLastModified"
                        value={formData.allocationStatusLastModified || ''}
                        readOnly
                    />
                </div>

                <div>
                    <label>Last Job:</label>
                    <input
                        type="text"
                        name="lastJob"
                        value={formData.lastJob || ''}
                        readOnly
                    />
                </div>

                {isCertified && (
                    <>
                        <div>
                            <label>Certification Status:</label>
                            <input
                                type="text"
                                name="certificationStatus"
                                value={formData.certificationStatus || ''}
                                readOnly
                            />
                        </div>
                        <div>
                            <label>Certification Date:</label>
                            <input
                                type="date"
                                name="certificationDate"
                                value={formData.certificationDate || ''}
                                readOnly={!isEditing}
                                onChange={handleInputChange}
                            />
                        </div>
                        <div>
                            <label>Certification Period (months):</label>
                            <select
                                name="certificationPeriod"
                                value={formData.certificationPeriod || ''}
                                disabled={!isEditing}
                                onChange={handleInputChange}
                            >
                                <option value="6">6 months</option>
                                <option value="12">12 months</option>
                                <option value="24">24 months</option>
                                <option value="36">36 months</option>
                                <option value="48">48 months</option>
                                <option value="60">60 months</option>
                            </select>
                        </div>
                        <div>
                            <label>Next Certification Date:</label>
                            <input
                                type="date"
                                name="nextCertificationDate"
                                value={formData.nextCertificationDate || ''}
                                readOnly
                            />
                        </div>
                        <div>
                            <label>Certificate File:</label>
                            <input
                                type="text"
                                name="fileCertificate"
                                value={formData.fileCertificate || ''}
                                readOnly={!isEditing}
                                onChange={handleInputChange}
                            />
                        </div>
                    </>
                )}
            </form>

            {nonFieldErrors.length > 0 && (
                <div className="error-summary" style={{ color: 'red' }}>
                    {nonFieldErrors.map((error, index) => (
                        <div key={index}>
                            {error}
                        </div>
                    ))}
                </div>
            )}

            {validationErrors.certificationStatus && (
                <div className="error" style={{ color: 'red' }}>
                    {validationErrors.certificationStatus}
                </div>
            )}

            {!isEditing && (
                <button onClick={() => setIsEditing(true)}>Edit</button>
            )}

            {isEditing && (
                <button onClick={handleSave}>Save</button>
            )}
        </div>
    );
};

export default EquipmentEditPage;