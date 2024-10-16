package org.example.trackit.Mapper;

import lombok.AllArgsConstructor;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.Equipment;
import org.example.trackit.entity.properties.Job;
import org.example.trackit.entity.properties.PartNumber;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EquipmentMapper {

    private final PartNumberMapper partNumberMapper;
    private final JobMapper jobMapper;

    public EquipmentDTO toDTO(Equipment equipment) {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(equipment.getId());
        dto.setSerialNumber(equipment.getSerialNumber());
        dto.setAllocationStatus(equipment.getAllocationStatus());
        dto.setHealthStatus(equipment.getHealthStatus());
        dto.setCreatedAt(equipment.getCreatedAt());
        dto.setAllocationStatusLastModified(equipment.getAllocationStatusLastModified());

        PartNumber partNumber = equipment.getPartNumber();
        dto.setPartNumberDTO(partNumberMapper.toDTO(partNumber));

        if (equipment.getJob() != null) {
            Job job = equipment.getJob();
            dto.setJobResponseDTO(jobMapper.toResponseDTO(job));
        }

        return dto;
    }

    public Equipment toEntity(EquipmentDTO dto) {
        Equipment entity = new Equipment();
        entity.setId(dto.getId());
        entity.setSerialNumber(dto.getSerialNumber());
        entity.setHealthStatus(dto.getHealthStatus());
        entity.setAllocationStatus(dto.getAllocationStatus());
        entity.setAllocationStatusLastModified(dto.getAllocationStatusLastModified());

        entity.setJob(jobMapper.toJob(dto.getJobResponseDTO()));

        PartNumber partNumber = new PartNumber();
        partNumber.setNumber(dto.getPartNumber());
        entity.setPartNumber(partNumber);
        return entity;
    }

}
