package org.example.trackit.mapper;

import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.entity.CertifiedEquipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;


@Component
@Mapper(componentModel = "spring", uses = {PartNumberMapper.class, JobMapper.class})
public interface CertifiedEquipmentMapper {

    @Mapping(target = "partNumberDTO", source = "partNumber")
    @Mapping(target = "jobResponseDTO", source = "job", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    CertifiedEquipmentDTO toDTO(CertifiedEquipment certifiedEquipment);

    @Mapping(target = "partNumber", source = "partNumberDTO")
    @Mapping(target = "job", source = "jobResponseDTO", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    CertifiedEquipment toEntity(CertifiedEquipmentDTO dto);
}
//    private final PartNumberMapper partNumberMapper;
//    private final JobMapper jobMapper;
//
//    public CertifiedEquipmentDTO toDTO(CertifiedEquipment equipment) {
//        CertifiedEquipmentDTO dto = new CertifiedEquipmentDTO();
//
//        dto.setId(equipment.getId());
//        dto.setSerialNumber(equipment.getSerialNumber());
//        dto.setHealthStatus(equipment.getHealthStatus());
//        dto.setAllocationStatus(equipment.getAllocationStatus());
//        dto.setAllocationStatusLastModified(equipment.getAllocationStatusLastModified());
//        dto.setCreatedAt(equipment.getCreatedAt());
//
//        PartNumber partNumber = equipment.getPartNumber();
//        dto.setPartNumberDTO(partNumberMapper.toDTO(partNumber));
//
//        if (equipment.getJob() != null) {
//            dto.setJobResponseDTO(jobMapper.toResponseDTO(equipment.getJob()));
//        }
//        dto.setLastJob(equipment.getLastJob());
//
//        dto.setComments(equipment.getComments());
//
//        dto.setCertificationStatus(equipment.getCertificationStatus());
//        dto.setCertificationDate(equipment.getCertificationDate());
//        dto.setCertificationPeriod(equipment.getCertificationPeriod());
//        dto.setNextCertificationDate(equipment.getNextCertificationDate());
//        dto.setFileCertificate(equipment.getFileCertificate());
//
//        return dto;
//    }
//
//    public CertifiedEquipment toEntity(CertifiedEquipmentDTO dto) {
//
//        CertifiedEquipment entity = new CertifiedEquipment();
//        entity.setId(dto.getId());
//        entity.setSerialNumber(dto.getSerialNumber());
//        entity.setHealthStatus(dto.getHealthStatus());
//        entity.setAllocationStatus(dto.getAllocationStatus());
//        entity.setAllocationStatusLastModified(dto.getAllocationStatusLastModified());
//
//        PartNumberDTO partNumberDTO = dto.getPartNumberDTO();
//        entity.setPartNumber(partNumberMapper.toEntity(partNumberDTO));
//
//        if (dto.getJobResponseDTO() != null) {
//            JobResponseDTO jobResponseDTO = dto.getJobResponseDTO();
//            entity.setJob(jobMapper.toJob(jobResponseDTO));
//        }
//        entity.setCertificationDate(dto.getCertificationDate());
//        entity.setNextCertificationDate(dto.getNextCertificationDate());
//        entity.setCertificationPeriod(dto.getCertificationPeriod());
//        entity.setFileCertificate(dto.getFileCertificate());
//
//        entity.setComments(dto.getComments());
//
//        return entity;
//    }
