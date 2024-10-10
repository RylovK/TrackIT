package org.example.trackit.Mapper;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.dto.PartNumberDTO;
import org.example.trackit.entity.Equipment;
import org.example.trackit.entity.properties.PartNumber;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {



        default EquipmentDTO toDTO(Equipment equipment) {
                if (equipment == null) {
                        return null;
                }
                EquipmentDTO dto = new EquipmentDTO();
                dto.setId(equipment.getId());
                dto.setSerialNumber(equipment.getSerialNumber());
                dto.setHealthStatus(equipment.getHealthStatus());
                dto.setAllocationStatus(equipment.getAllocationStatus());
                dto.setJob(equipment.getJob());
                dto.setCreatedAt(equipment.getCreatedAt());
                dto.setAllocationStatusLastModified(equipment.getAllocationStatusLastModified());

                PartNumberDTO partNumberDTO = new PartNumberDTO();
                partNumberDTO.setNumber(equipment.getPartNumber().getNumber());
                partNumberDTO.setDescription(equipment.getPartNumber().getDescription());
                partNumberDTO.setPhoto(equipment.getPartNumber().getPhoto());
                dto.setPartNumberDTO(partNumberDTO);
                return dto;

        }

        default Equipment toEntity(EquipmentDTO equipmentDTO) {
                if (equipmentDTO == null) {
                        return null;
                }
                Equipment entity = new Equipment();
                entity.setId(equipmentDTO.getId());
                entity.setSerialNumber(equipmentDTO.getSerialNumber());
                entity.setHealthStatus(equipmentDTO.getHealthStatus());
                entity.setAllocationStatus(equipmentDTO.getAllocationStatus());
                entity.setJob(equipmentDTO.getJob());
                entity.setAllocationStatusLastModified(equipmentDTO.getAllocationStatusLastModified());

                PartNumber partNumber = new PartNumber();
                partNumber.setNumber(equipmentDTO.getPartNumber());
                partNumber.setDescription(equipmentDTO.getDescription());
                partNumber.setPhoto(equipmentDTO.getPhoto());
                entity.setPartNumber(partNumber);
                return entity;
        }

}
