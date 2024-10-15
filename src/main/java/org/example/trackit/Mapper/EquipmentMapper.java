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
//                if (equipment == null) {
//                        return null;
//                }
                EquipmentDTO dto = new EquipmentDTO();
                dto.setId(equipment.getId());
                dto.setSerialNumber(equipment.getSerialNumber());
                dto.setHealthStatus(equipment.getHealthStatus());
                dto.setAllocationStatus(equipment.getAllocationStatus());
                dto.setCreatedAt(equipment.getCreatedAt());
                dto.setAllocationStatusLastModified(equipment.getAllocationStatusLastModified());

                PartNumber partNumber = equipment.getPartNumber();
                dto.setPartNumberDTO(partNumberMapper.toDTO(partNumber));

                Job job = equipment.getJob();
                dto.setJobDTO(jobMapper.toDTO(job));

                return dto;
        }

        public Equipment toEntity(EquipmentDTO dto) {
//                if (equipmentDTO == null) {
//                        return null;
//                }
                Equipment entity = new Equipment();
                entity.setId(dto.getId());
                entity.setSerialNumber(dto.getSerialNumber());
                entity.setHealthStatus(dto.getHealthStatus());
                entity.setAllocationStatus(dto.getAllocationStatus());
                entity.setAllocationStatusLastModified(dto.getAllocationStatusLastModified());

                entity.setJob(new Job(dto.getJobName()));

                PartNumber partNumber = new PartNumber();
                partNumber.setNumber(dto.getPartNumber());
//                partNumber.setDescription(dto.getDescription());
//                partNumber.setPhoto(dto.getPhoto());
                entity.setPartNumber(partNumber);
                return entity;
        }

}
