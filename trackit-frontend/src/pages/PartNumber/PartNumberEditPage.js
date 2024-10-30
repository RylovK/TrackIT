import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Card, Typography, Image, Spin, Button, Input, message } from 'antd';
import api from '../../api';

const { Title, Text } = Typography;

const PartNumberPage = () => {
    const { partNumber } = useParams();
    const navigate = useNavigate();
    const [partNumberData, setPartNumberData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [isEditing, setIsEditing] = useState(false);
    const [editedData, setEditedData] = useState({ number: '', description: '', photo: '' });
    const [imageFile, setImageFile] = useState(null);
    const [partNumbers, setPartNumbers] = useState([]); // Состояние для всех номеров деталей

    useEffect(() => {
        const fetchPartNumberData = async () => {
            try {
                const response = await api.get(`/partNumber/${partNumber}`);
                setPartNumberData(response.data);
                setEditedData({
                    number: response.data.number,
                    description: response.data.description,
                    photo: response.data.photo,
                });
            } catch (error) {
                if (error.response) {
                    message.error('Failed to fetch part number data: ' + error.response.data.message);
                } else {
                    message.error('Failed to fetch part number data');
                }
            } finally {
                setLoading(false);
            }
        };

        const fetchAllPartNumbers = async () => {
            try {
                const response = await api.get('/partNumber'); // Запрос на получение всех номеров деталей
                setPartNumbers(response.data); // Сохраняем данные в состояние
            } catch (error) {
                message.error('Failed to fetch part numbers');
            }
        };

        fetchPartNumberData();
        fetchAllPartNumbers();
    }, [partNumber]);

    const handleEditClick = () => {
        setIsEditing(true);
    };

    const handleSaveClick = async () => {
        try {
            const response = await api.patch(`/partNumber/${partNumber}`, editedData);
            message.success('Part number updated successfully');

            if (editedData.number !== partNumberData.number) {
                navigate(`/partnumber/${editedData.number}`);
            } else {
                setPartNumberData(response.data);
                setIsEditing(false);
            }
        } catch (error) {
            if (error.response) {
                message.error('Failed to update part number: ' + error.response.data.message);
            } else {
                message.error('Failed to update part number');
            }
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setEditedData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    const handleImageUploadChange = (e) => {
        setImageFile(e.target.files[0]);
    };

    const handleImageUpload = async () => {
        if (!imageFile) {
            message.warning('Please select an image to upload.');
            return;
        }

        const formData = new FormData();
        formData.append('file', imageFile);

        try {
            await api.patch(`/partNumber/${partNumber}/upload`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            message.success('Image uploaded successfully');
            window.location.reload();
        } catch (error) {
            if (error.response) {
                message.error('Failed to upload image: ' + error.response.data.message);
            } else {
                message.error('Failed to upload image');
            }
        }
    };

    const handlePartNumberSelect = (e) => {
        const value = e.target.value;
        setEditedData((prevData) => ({
            ...prevData,
            number: value,
        }));

        // Проверяем, существует ли номер детали в списке
        if (partNumbers.some(part => part.number === value)) {
            navigate(`/partnumber/${value}`);
        // } else {
        //     message.warning('Part number does not exist'); // Сообщение, если номер не найден
        }
    };

    const handlePartNumberBlur = () => {
        if (partNumbers.some(part => part.number === editedData.number)) {
            navigate(`/partnumber/${editedData.number}`);
        } else {
            message.warning('Part number does not exist');
        }
    };



    const handleDeleteClick = async () => {
        try {
            const response = await api.delete(`/partNumber?partNumber=${partNumber}`);
            if (response.status === 200) {
                message.success('Part number deleted successfully');
                navigate('/partnumber/all');
            } else {
                message.error('Failed to delete part number');
            }
        } catch (error) {
            if (error.response) {
                message.error('Failed to delete part number: ' + error.response.data.message);
            } else {
                message.error('Failed to delete part number');
            }
        }
    };

    if (loading) return <Spin size="large" />;

    return (
        <Card style={{ width: 300, margin: 'auto' }}>
            <Input
                list="partNumbers"
                placeholder="Select Part Number"
                value={editedData.number}
                onChange={handlePartNumberSelect}
                onBlur={handlePartNumberBlur}
                style={{ width: '100%', marginTop: 16 }}
            />
            <datalist id="partNumbers">
                {partNumbers.map((part) => (
                    <option key={part.number} value={part.number} />
                ))}
            </datalist>
            <Title level={4}>
                Part Number: {isEditing ? (
                <Input
                    name="number"
                    value={editedData.number}
                    onChange={handleChange}
                    style={{ marginBottom: 16 }}
                />
            ) : (
                partNumberData.number
            )}
            </Title>
            <Text>Description: {isEditing ? (
                <Input
                    name="description"
                    value={editedData.description}
                    onChange={handleChange}
                    style={{ marginBottom: 16 }}
                />
            ) : (
                partNumberData.description
            )}</Text>
            {partNumberData.photo && (
                <Image
                    width={200}
                    src={`http://localhost:8080/images/${partNumberData.photo}`}
                    alt="Part number image"
                    style={{ marginTop: 16 }}
                />
            )}

            {!isEditing ? (
                <Button type="primary" onClick={handleEditClick} style={{ marginTop: 16 }}>
                    Edit
                </Button>
            ) : (
                <Button type="primary" onClick={handleSaveClick} style={{ marginTop: 16 }}>
                    Save
                </Button>
            )}
            <Button type="danger" onClick={handleDeleteClick} style={{ marginTop: 16, marginLeft: 10 }}>
                Delete
            </Button>
            <input type="file" onChange={handleImageUploadChange} style={{ marginTop: 16 }} />
            <Button type="primary" onClick={handleImageUpload} style={{ marginTop: 16 }}>
                Upload Image
            </Button>
        </Card>
    );
};

export default PartNumberPage;
