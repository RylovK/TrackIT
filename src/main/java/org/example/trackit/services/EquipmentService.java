package org.example.trackit.services;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.example.trackit.Mapper.CertifiedEquipmentMapper;
import org.example.trackit.Mapper.EquipmentMapper;
import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.CertifiedEquipment;
import org.example.trackit.entity.Equipment;
import org.example.trackit.entity.properties.AllocationStatus;
import org.example.trackit.entity.properties.HealthStatus;
import org.example.trackit.entity.properties.PartNumber;
import org.example.trackit.repository.EquipmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;
    private final CertifiedEquipmentMapper certifiedEquipmentMapper;
    private final PartNumberService partNumberService;

    public Page<EquipmentDTO> findAllEquipment
            (String partNumber, String serialNumber, HealthStatus healthStatus, AllocationStatus allocationStatus, String jobName, Pageable pageable) {
        Specification<Equipment> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (partNumber != null) {
                predicates.add(criteriaBuilder.like(root.get("partNumber"), "%" + partNumber + "%"));
            }
            if (serialNumber != null) {
                predicates.add(criteriaBuilder.like(root.get("serialNumber"), "%" + serialNumber + "%"));
            }
            if (healthStatus != null) {
                predicates.add(criteriaBuilder.equal(root.get("healthStatus"), healthStatus));
            }
            if (allocationStatus != null) {
                predicates.add(criteriaBuilder.equal(root.get("allocationStatus"), allocationStatus));
            }
            if (jobName != null && !jobName.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.join("job").get("jobName"), jobName));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page<Equipment> page = equipmentRepository.findAll(spec, pageable);
        return page.map(equipmentMapper::toDTO);
    }

    public EquipmentDTO createEquipment(EquipmentDTO equipmentDTO) {
        Optional<PartNumber> partNumber = partNumberService.findPartNumberByNumber(equipmentDTO.getPartNumber());
        Equipment equipment = new Equipment(partNumber.get(), equipmentDTO.getSerialNumber());
        partNumber.get().getEquipmentList().add(equipment);
        equipmentRepository.save(equipment);
        return equipmentMapper.toDTO(equipment);
    }

    public EquipmentDTO findEquipmentById(int id) {
        Optional<Equipment> byId = equipmentRepository.findById(id);
        return byId.map(equipmentMapper::toDTO).orElse(null);//TODO:сделать exception?
    }

    public Page<CertifiedEquipmentDTO> findAllCertifiedEquipment(Pageable pageable) {
        Page<CertifiedEquipment> founded = equipmentRepository.findAllCertifiedEquipment(pageable);
        return founded.map(certifiedEquipmentMapper::toDTO);
    }
    public Optional<Equipment> findByPartNumberAndSerialNumber(@NotEmpty String partNumber, String serialNumber) {
        return equipmentRepository.findByPartNumberAndSerialNumber(partNumber, serialNumber);
    }
}
