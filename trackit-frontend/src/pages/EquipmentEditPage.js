import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';

const EquipmentEditPage = () => {
    const { id } = useParams();
    const [equipment, setEquipment] = useState(null);
    const [isCertified, setIsCertified] = useState(false);
    const [isEditing, setIsEditing] = useState(false); // Флаг редактирования
    const [formData, setFormData] = useState({}); // Состояние для формы

    useEffect(() => {
        // Извлекаем JWT токен из localStorage
        const token = localStorage.getItem('token');

        // Получаем оборудование по ID с токеном
        axios.get(`http://localhost:8080/equipment/${id}`, {
            headers: {
                'Authorization': `Bearer ${token}` // JWT токен в заголовке
            }
        })
            .then(response => {
                setEquipment(response.data);
                setFormData(response.data); // Устанавливаем данные в форму
                if (response.data.certificationStatus) {
                    setIsCertified(true); // Проверка сертифицированного оборудования
                }
            })
            .catch(error => {
                console.error('Error fetching equipment:', error);
            });
    }, [id]);

    // Обработка изменения значений в форме
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    // Обработка сохранения изменений
    const handleSave = () => {
        const token = localStorage.getItem('token');
        const url = isCertified
            ? `http://localhost:8080/certified/${id}`
            : `http://localhost:8080/equipment/${id}`;

        axios.patch(url, formData, {
            headers: {
                'Authorization': `Bearer ${token}`, // JWT токен в заголовке
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                console.log("Equipment updated successfully:", response.data);
                setIsEditing(false); // Отключаем режим редактирования
                window.location.reload();
            })
            .catch(error => {
                console.error("Error updating equipment:", error);
            });
    };

    if (!equipment) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <h1>Equipment Info</h1>
            <form>
                {/* Общие поля для Equipment */}
                <div>
                    <label>Serial Number:</label>
                    <input
                        type="text"
                        name="serialNumber"
                        value={formData.serialNumber || ''}
                        readOnly={!isEditing}
                        onChange={handleInputChange}
                    />
                </div>
                <div>
                    <label>Part Number:</label>
                    <input
                        type="text"
                        name="partNumber"
                        value={formData.partNumber || ''}
                        readOnly={!isEditing}
                        onChange={handleInputChange}
                    />
                </div>
                <div>
                    <label>Health Status:</label>
                    <select
                        name="healthStatus"
                        value={formData.healthStatus || ''}
                        disabled={!isEditing} // Disable when not editing
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
                        disabled={!isEditing} // Disable when not editing
                        onChange={handleInputChange}
                    >
                        <option value="ON_LOCATION">ON_LOCATION</option>
                        <option value="ON_BASE">ON_BASE</option>
                    </select>
                </div>
                <div>
                    <label>Last Job:</label>
                    <input
                        type="text"
                        name="lastJob"
                        value={formData.lastJob || ''}
                        readOnly={!isEditing}
                        onChange={handleInputChange}
                    />
                </div>

                {/* Поля, специфичные для CertifiedEquipment */}
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
                            <input
                                type="number"
                                name="certificationPeriod"
                                value={formData.certificationPeriod || ''}
                                readOnly={!isEditing}
                                onChange={handleInputChange}
                            />
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

            {/* Кнопка Edit */}
            {!isEditing && (
                <button onClick={() => setIsEditing(true)}>
                    Edit
                </button>
            )}

            {/* Кнопка Save */}
            {isEditing && (
                <button onClick={handleSave}>
                    Save
                </button>
            )}
        </div>
    );
};

export default EquipmentEditPage;
