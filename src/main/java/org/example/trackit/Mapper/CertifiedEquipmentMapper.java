package org.example.trackit.Mapper;

import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.dto.JobDTO;
import org.example.trackit.dto.PartNumberDTO;
import org.example.trackit.entity.CertifiedEquipment;
import org.example.trackit.entity.Equipment;
import org.example.trackit.entity.properties.CertificationStatus;
import org.example.trackit.entity.properties.Job;
import org.example.trackit.entity.properties.PartNumber;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.Period;

@Mapper(componentModel = "spring")
public interface CertifiedEquipmentMapper {

    default CertifiedEquipmentDTO toDTO(CertifiedEquipment equipment) {
        CertifiedEquipmentDTO dto = new CertifiedEquipmentDTO();
        dto.setId(equipment.getId());
        dto.setSerialNumber(equipment.getSerialNumber());
        dto.setHealthStatus(equipment.getHealthStatus());
        dto.setHealthStatus(equipment.getHealthStatus());
        dto.setJob(new JobDTO(equipment.getJob().getJobName()));
        dto.setCreatedAt(equipment.getCreatedAt());
        dto.setAllocationStatusLastModified(equipment.getAllocationStatusLastModified());

        dto.setCertificationStatus(equipment.getCertificationStatus());
        dto.setCertificationDate(equipment.getCertificationDate());
        dto.setCertificationPeriod(equipment.getCertificationPeriod());
        dto.setNextCertificationDate(equipment.getNextCertificationDate());
        dto.setFileCertificate(equipment.getFileCertificate());

        PartNumberDTO partNumberDTO = new PartNumberDTO();
        partNumberDTO.setNumber(equipment.getPartNumber().getNumber());
        partNumberDTO.setDescription(equipment.getPartNumber().getDescription());
        partNumberDTO.setPhoto(equipment.getPartNumber().getPhoto());
        dto.setPartNumberDTO(partNumberDTO);

        return dto;
    }

    default CertifiedEquipment toEntity(CertifiedEquipmentDTO equipmentDTO) {
        if (equipmentDTO == null) {
            return null;
        }
        CertifiedEquipment entity = new CertifiedEquipment();
        entity.setId(equipmentDTO.getId());
        entity.setSerialNumber(equipmentDTO.getSerialNumber());
        entity.setHealthStatus(equipmentDTO.getHealthStatus());
        entity.setAllocationStatus(equipmentDTO.getAllocationStatus());
        Job job = new Job();
        job.setJobName(equipmentDTO.getJob().getJobName());
        entity.setJob(job);
        entity.setAllocationStatusLastModified(equipmentDTO.getAllocationStatusLastModified());

        //entity.setCertificationStatus(equipmentDTO.getCertificationStatus());
        entity.setCertificationDate(equipmentDTO.getCertificationDate());
        entity.setCertificationPeriod(equipmentDTO.getCertificationPeriod());
        //entity.setNextCertificationDate(equipmentDTO.getNextCertificationDate());
        entity.setFileCertificate(equipmentDTO.getFileCertificate());

        PartNumber partNumber = new PartNumber();
        partNumber.setNumber(equipmentDTO.getPartNumber());
        partNumber.setDescription(equipmentDTO.getDescription());
        partNumber.setPhoto(equipmentDTO.getPhoto());
        entity.setPartNumber(partNumber);

        return entity;
    }
}
