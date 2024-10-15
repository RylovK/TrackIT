package org.example.trackit.Mapper;

import lombok.AllArgsConstructor;
import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.dto.JobDTO;
import org.example.trackit.dto.PartNumberDTO;
import org.example.trackit.entity.CertifiedEquipment;
import org.example.trackit.entity.properties.Job;
import org.example.trackit.entity.properties.PartNumber;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class CertifiedEquipmentMapper {

    private final PartNumberMapper partNumberMapper;
    private final JobMapper jobMapper;

    public CertifiedEquipmentDTO toDTO(CertifiedEquipment equipment) {
        CertifiedEquipmentDTO dto = new CertifiedEquipmentDTO();

        dto.setId(equipment.getId());
        dto.setSerialNumber(equipment.getSerialNumber());
        dto.setHealthStatus(equipment.getHealthStatus());
        dto.setAllocationStatus(equipment.getAllocationStatus());
        dto.setAllocationStatusLastModified(equipment.getAllocationStatusLastModified());
        dto.setCreatedAt(equipment.getCreatedAt());

        PartNumber partNumber = equipment.getPartNumber();
        dto.setPartNumberDTO(partNumberMapper.toDTO(partNumber));

        dto.setJobDTO(jobMapper.toDTO(equipment.getJob()));

        dto.setCertificationStatus(equipment.getCertificationStatus());
        dto.setCertificationDate(equipment.getCertificationDate());
        dto.setCertificationPeriod(equipment.getCertificationPeriod());
        dto.setNextCertificationDate(equipment.getNextCertificationDate());
        dto.setFileCertificate(equipment.getFileCertificate());

        return dto;
    }

    public CertifiedEquipment toEntity(CertifiedEquipmentDTO dto) {
//        if (equipmentDTO == null) {
//            return null;
//        }
        CertifiedEquipment entity = new CertifiedEquipment();
        entity.setId(dto.getId());
        entity.setSerialNumber(dto.getSerialNumber());
        entity.setHealthStatus(dto.getHealthStatus());
        entity.setAllocationStatus(dto.getAllocationStatus());
        entity.setAllocationStatusLastModified(dto.getAllocationStatusLastModified());

        PartNumberDTO partNumberDTO = dto.getPartNumberDTO();
        entity.setPartNumber(partNumberMapper.toEntity(partNumberDTO));

        JobDTO jobDTO = dto.getJobDTO();
        entity.setJob(jobMapper.toJob(jobDTO));

        //entity.setCertificationStatus(equipmentDTO.getCertificationStatus());
        entity.setCertificationDate(dto.getCertificationDate());
        entity.setCertificationPeriod(dto.getCertificationPeriod());
        //entity.setNextCertificationDate(equipmentDTO.getNextCertificationDate());
        entity.setFileCertificate(dto.getFileCertificate());

        return entity;
    }
}
