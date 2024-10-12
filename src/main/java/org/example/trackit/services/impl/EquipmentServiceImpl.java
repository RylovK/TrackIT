package org.example.trackit.services.impl;

import jakarta.persistence.criteria.Predicate;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.example.trackit.Mapper.EquipmentMapper;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.Equipment;
import org.example.trackit.entity.properties.*;
import org.example.trackit.repository.EquipmentRepository;
import org.example.trackit.services.EquipmentService;
import org.example.trackit.services.PartNumberService;
import org.example.trackit.util.exceptions.PartNumberNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;
    private final PartNumberService partNumberService;


    @Override
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

    @Override
    @Transactional
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

    @Override
    public EquipmentDTO findEquipmentById(int id) {
        Optional<Equipment> byId = equipmentRepository.findById(id);
        return byId.map(equipmentMapper::toDTO).orElse(null);//TODO:сделать exception?
    }

    @Override
    public Optional<Equipment> findByPartNumberAndSerialNumber(@NotEmpty String partNumber, String serialNumber) {
        return equipmentRepository.findByPartNumberAndSerialNumber(partNumber, serialNumber);
    }

    @Override
    public boolean deleteEquipmentById(int id) {
        Optional<Equipment> byId = equipmentRepository.findById(id);
        if (byId.isPresent()) {
            equipmentRepository.delete(byId.get());
            return true;
        }
        return false;
    }
}
