package org.example.trackit.services.impl;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.example.trackit.Mapper.CertifiedEquipmentMapper;
import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.entity.CertifiedEquipment;
import org.example.trackit.entity.Equipment;
import org.example.trackit.entity.properties.*;
import org.example.trackit.repository.EquipmentRepository;
import org.example.trackit.util.exceptions.PartNumberNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CertifiedEquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final CertifiedEquipmentMapper certifiedEquipmentMapper;
    private final PartNumberService partNumberService;

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

    public CertifiedEquipmentDTO findCertifiedEquipmentById(Integer id) {
        Optional<Equipment> founded = equipmentRepository.findCertifiedEquipmentById(id);
        if (founded.isEmpty()) {
            return null;
        }
        CertifiedEquipment result = (CertifiedEquipment) founded.get();
        return certifiedEquipmentMapper.toDTO(result);
    }
    @Transactional
    public CertifiedEquipmentDTO save(CertifiedEquipmentDTO dto) {
        Optional<PartNumber> partNumber = partNumberService.findPartNumberByNumber(dto.getPartNumber());
        if (partNumber.isEmpty()) {
            throw new PartNumberNotFoundException("PartNumber does not exist");
        }
        CertifiedEquipment equipment;
        if(dto.getNextCertificationDate() == null) {
            equipment = new CertifiedEquipment(partNumber.get(), dto.getSerialNumber());

        } else {
            equipment = new CertifiedEquipment(partNumber.get(), dto.getSerialNumber(), dto.getCertificationDate(), dto.getCertificationPeriod(),dto.getFileCertificate());
        }
        partNumber.get().getEquipmentList().add(equipment);
        equipmentRepository.save(equipment);
        return certifiedEquipmentMapper.toDTO(equipment);
    }
}
