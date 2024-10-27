import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../api';

const EquipmentCreatingPage = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        serialNumber: '',
        partNumber: '',
    });
    const [partNumbers, setPartNumbers] = useState([]);
    const [validationErrors, setValidationErrors] = useState({});
    const [nonFieldErrors, setNonFieldErrors] = useState([]);
    const [isCertified, setIsCertified] = useState(false); // Флаг для сертифицированного оборудования

    useEffect(() => {
        api.get('/partNumber')
            .then(response => {
                setPartNumbers(response.data);
            })
            .catch(error => {
                console.error('Error fetching part numbers:', error);
            });
    }, []);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevFormData) => ({ ...prevFormData, [name]: value }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        const url = isCertified ? '/certified' : '/equipment'; // Выбор URL на основе состояния галочки

        api.post(url, formData)
            .then(response => {
                console.log('Equipment created successfully:', response.data);
                navigate(`/equipment/${response.data.id}`);
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

                    setValidationErrors(fieldErrors);
                    setNonFieldErrors(newNonFieldErrors);
                } else {
                    console.error('Error creating equipment:', error);
                }
            });
    };

    return (
        <div>
            <h1>Create Equipment</h1>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Serial Number:</label>
                    <input
                        type="text"
                        name="serialNumber"
                        value={formData.serialNumber}
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
                        value={formData.partNumber}
                        onChange={handleInputChange}
                    >
                        <option value="" disabled>Select a part number</option>
                        {partNumbers.map(part => (
                            <option key={part.id} value={part.number}>
                                {part.number} - {part.description}
                            </option>
                        ))}
                    </select>
                    {validationErrors.partNumber && (
                        <p className="error">{validationErrors.partNumber}</p>
                    )}
                </div>

                <div>
                    <label>Certified Equipment:</label>
                    <input
                        type="checkbox"
                        checked={isCertified}
                        onChange={(e) => setIsCertified(e.target.checked)}
                    />
                </div>

                <button type="submit">Create Equipment</button>

                {nonFieldErrors.length > 0 && (
                    <div className="error-summary" style={{ color: 'red' }}>
                        {nonFieldErrors.map((error, index) => (
                            <div key={index}>{error}</div>
                        ))}
                    </div>
                )}
            </form>
        </div>
    );
};

export default EquipmentCreatingPage;
