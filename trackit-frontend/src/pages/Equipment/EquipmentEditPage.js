import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../../api'; // Импортируем ваш API

const EquipmentEditPage = () => {
    const { id } = useParams();
    const navigate = useNavigate(); // Для навигации после удаления
    const [equipment, setEquipment] = useState(null);
    const [isCertified, setIsCertified] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const [formData, setFormData] = useState({});
    const [validationErrors, setValidationErrors] = useState({});
    const [nonFieldErrors, setNonFieldErrors] = useState([]);
    const [partNumbers, setPartNumbers] = useState([]);
    const imageDirectory = 'http://localhost:8080/images/';
    const [jobs, setJobs] = useState([]);
    const [certificateFile, setCertificateFile] = useState(null); // Состояние для загружаемого файла
    const [successMessage, setSuccessMessage] = useState('');

    useEffect(() => {
        fetchEquipment();
        fetchPartNumbers();
        fetchJobs();
    }, [id]);

    const fetchEquipment = async () => {
        try {
            const response = await api.get(`/equipment/${id}`);
            setEquipment(response.data);
            setFormData(response.data);
            if (response.data.certificationStatus) {
                setIsCertified(true);
            }
        } catch (error) {
            console.error('Error fetching equipment:', error);
        }
    };

    const fetchPartNumbers = async () => {
        try {
            const response = await api.get('/partNumber');
            setPartNumbers(response.data);
        } catch (error) {
            console.error('Error fetching part numbers:', error);
        }
    };

    const fetchJobs = async () => {
        try {
            const response = await api.get('/job');
            setJobs(response.data);
        } catch (error) {
            console.error('Error fetching jobs:', error);
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevFormData => ({ ...prevFormData, [name]: value }));
    };

    const handleSave = () => {
        const url = isCertified
            ? `/certified/${id}`
            : `/equipment/${id}`;

        api.patch(url, formData)
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

    // Метод для удаления оборудования
    const handleDelete = () => {
        api.delete(`/equipment/${id}`)
            .then(() => {
                console.log("Equipment deleted successfully");
                navigate('/equipment'); // Перенаправление на список оборудования после удаления
            })
            .catch(error => {
                console.error("Error deleting equipment:", error);
            });
    };

    // Метод для загрузки сертификата
    const handleUploadCertificate = async () => {
        if (!certificateFile) {
            alert('Please select a file to upload.');
            return;
        }

        const formData = new FormData();
        formData.append('file', certificateFile);

        try {
            const response = await api.patch(`/certified/${id}/upload`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            console.log('File uploaded successfully:', response.data);
            setSuccessMessage('File uploaded successfully!'); // Успешное сообщение
            setCertificateFile(null); // Сбросить файл после успешной загрузки
            await fetchEquipment(); // Обновить данные оборудования
        } catch (error) {
            console.error('Error uploading file:', error);
        }
    };

    const handleDownloadCertificate = () => {
        if (formData.fileCertificate) {
            // Предполагается, что formData.fileCertificate содержит только имя файла
            const downloadUrl = `http://localhost:8080/certificates/${formData.fileCertificate}`;
            window.open(downloadUrl, '_blank');
        } else {
            alert('No certificate file available for download.');
        }
    };


    if (!equipment) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <h1>Equipment Info</h1>
            {successMessage && <div style={{ color: 'green' }}>{successMessage}</div>}
            <form>
                {formData.photo && (
                    <div>
                        <label>Image:</label>
                        <img
                            src={`${imageDirectory}${formData.photo}`}
                            alt={formData.serialNumber || 'Equipment Image'}
                            style={{ width: '200px', height: 'auto' }}
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
                    <label>Description:</label>
                    <input
                        type="text"
                        name="description"
                        value={formData.description || ''}
                        readOnly
                    />
                    {validationErrors.description && (
                        <p className="error">{validationErrors.description}</p>
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
                            <button type="button" onClick={handleDownloadCertificate}>
                                Download Certificate
                            </button>
                            <input
                                type="file"
                                onChange={(e) => setCertificateFile(e.target.files[0])}
                                style={{ marginLeft: '10px' }}
                            />
                            <button type="button" onClick={handleUploadCertificate}>
                                Upload Certificate
                            </button>
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
            <button onClick={handleDelete} style={{ color: 'red', marginTop: '10px' }}>
                Delete Equipment
            </button>
        </div>
    );
};

export default EquipmentEditPage;
