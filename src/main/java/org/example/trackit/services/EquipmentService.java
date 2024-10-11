package org.example.trackit.services;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.example.trackit.Mapper.CertifiedEquipmentMapper;
import org.example.trackit.Mapper.EquipmentMapper;
import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.CertifiedEquipment;
import org.example.trackit.entity.Equipment;
import org.example.trackit.entity.properties.*;
import org.example.trackit.repository.EquipmentRepository;
import org.example.trackit.util.exceptions.PartNumberNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;
    private final CertifiedEquipmentMapper certifiedEquipmentMapper;
    private final PartNumberService partNumberService;

    public Page<EquipmentDTO> findAllEquipment
            (String partNumber, String serialNumber, HealthStatus healthStatus,
             AllocationStatus allocationStatus, String jobName, Pageable pageable) {
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

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public EquipmentDTO save(EquipmentDTO equipmentDTO) {
        Optional<PartNumber> partNumber = partNumberService.findPartNumberByNumber(equipmentDTO.getPartNumber());
        if (partNumber.isEmpty()) {
            throw new PartNumberNotFoundException("PartNumber does not exist");
        }
        Equipment equipment = new Equipment(partNumber.get(), equipmentDTO.getSerialNumber());
        partNumber.get().getEquipmentList().add(equipment);
        equipmentRepository.save(equipment);
        return equipmentMapper.toDTO(equipment);
    }

    public EquipmentDTO findEquipmentById(int id) {
        Optional<Equipment> byId = equipmentRepository.findById(id);
        return byId.map(equipmentMapper::toDTO).orElse(null);//TODO:сделать exception?
    }

    public Page<CertifiedEquipmentDTO> findAllCertifiedEquipment
            (String partNumber, String serialNumber, HealthStatus healthStatus,
             AllocationStatus allocationStatus, String jobName,
             CertificationStatus certificationStatus, Pageable pageable) {
        Specification<CertifiedEquipment> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(partNumber)) {
                predicates.add(criteriaBuilder.like(root.get("partNumber"), "%" + partNumber + "%"));
            }
            if (StringUtils.hasText(serialNumber)) {
                predicates.add(criteriaBuilder.like(root.get("serialNumber"), "%" + serialNumber + "%"));
            }
            if (healthStatus != null) {
                predicates.add(criteriaBuilder.equal(root.get("healthStatus"), healthStatus));
            }
            if (allocationStatus != null) {
                predicates.add(criteriaBuilder.equal(root.get("allocationStatus"), allocationStatus));
            }
            if (StringUtils.hasText(jobName)) {
                // Пример фильтрации по связанному объекту Job
                Join<CertifiedEquipment, Job> jobJoin = root.join("job", JoinType.INNER);
                predicates.add(criteriaBuilder.equal(jobJoin.get("name"), jobName));
            }
            if (certificationStatus != null) {
                predicates.add(criteriaBuilder.equal(root.get("certificationStatus"), certificationStatus));
            };
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page<CertifiedEquipment> page = equipmentRepository.findAllCertifiedEquipment(spec, pageable);
        return page.map(certifiedEquipmentMapper::toDTO);
    }

    public Optional<Equipment> findByPartNumberAndSerialNumber(@NotEmpty String partNumber, String serialNumber) {
        return equipmentRepository.findByPartNumberAndSerialNumber(partNumber, serialNumber);
    }
}
