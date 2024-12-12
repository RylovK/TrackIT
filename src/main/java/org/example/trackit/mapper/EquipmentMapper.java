package org.example.trackit.mapper;

import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.Equipment;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {PartNumberMapper.class, JobMapper.class})
public interface EquipmentMapper {

    @Mapping(target = "partNumberDTO", source = "partNumber")
    @Mapping(target = "jobResponseDTO", source = "job", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    EquipmentDTO toDTO(Equipment equipment);

    @Mapping(target = "partNumber", source = "partNumberDTO")
    @Mapping(target = "job", source = "jobResponseDTO", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    Equipment toEquipment(EquipmentDTO equipmentDTO);
}
//    private final PartNumberMapper partNumberMapper;
//    private final JobMapper jobMapper;
//
//    public EquipmentDTO toDTO(Equipment equipment) {
//        EquipmentDTO dto = new EquipmentDTO();
//        dto.setId(equipment.getId());
//        dto.setSerialNumber(equipment.getSerialNumber());
//        dto.setAllocationStatus(equipment.getAllocationStatus());
//        dto.setHealthStatus(equipment.getHealthStatus());
//        dto.setCreatedAt(equipment.getCreatedAt());
//        dto.setAllocationStatusLastModified(equipment.getAllocationStatusLastModified());
//
//        PartNumber partNumber = equipment.getPartNumber();
//        dto.setPartNumberDTO(partNumberMapper.toDTO(partNumber));
//
//        if (equipment.getJob() != null) {
//            Job job = equipment.getJob();
//            dto.setJobResponseDTO(jobMapper.toResponseDTO(job));
//        }
//        dto.setLastJob(equipment.getLastJob());
//
//        dto.setComments(equipment.getComments());
//
//        return dto;
//    }
//
//    PartNumberDTO partNumberToDTO(PartNumber partNumber) {
//        return partNumberMapper.toDTO(partNumber);
//    }
//
//    //TODO: тут не все поля, нужны ли они?
//    public Equipment toEntity(EquipmentDTO dto) {
//        Equipment entity = new Equipment();
//        entity.setId(dto.getId());
//        entity.setSerialNumber(dto.getSerialNumber());
//        entity.setHealthStatus(dto.getHealthStatus());
//        entity.setAllocationStatus(dto.getAllocationStatus());
//        entity.setAllocationStatusLastModified(dto.getAllocationStatusLastModified());
//
//        if (dto.getJobName() != null && dto.getAllocationStatus() == AllocationStatus.ON_LOCATION) {
//            entity.setJob(jobMapper.toJob(dto.getJobResponseDTO()));
//        }
//        PartNumber partNumber = partNumberMapper.toEntity(dto.getPartNumberDTO());
//        entity.setPartNumber(partNumber);
//        entity.setComments(dto.getComments());
//        return entity;
//    }
