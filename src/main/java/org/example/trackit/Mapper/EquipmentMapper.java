package org.example.trackit.Mapper;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.Equipment;
import org.example.trackit.entity.properties.AllocationStatus;
import org.example.trackit.entity.properties.Job;
import org.example.trackit.entity.properties.PartNumber;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
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
        dto.setLastJob(equipment.getLastJob());

        dto.setComments(equipment.getComments());

        return dto;
    }

    //TODO: тут не все поля, нужны ли они?
    public Equipment toEntity(EquipmentDTO dto) {
        Equipment entity = new Equipment();
        entity.setId(dto.getId());
        entity.setSerialNumber(dto.getSerialNumber());
        entity.setHealthStatus(dto.getHealthStatus());
        entity.setAllocationStatus(dto.getAllocationStatus());
        entity.setAllocationStatusLastModified(dto.getAllocationStatusLastModified());

        if (dto.getJobName() != null && dto.getAllocationStatus() == AllocationStatus.ON_LOCATION) {
            entity.setJob(jobMapper.toJob(dto.getJobResponseDTO()));
        }
        PartNumber partNumber = partNumberMapper.toEntity(dto.getPartNumberDTO());
        entity.setPartNumber(partNumber);
        entity.setComments(dto.getComments());
        return entity;
    }
}
