package org.example.trackit.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;

import lombok.AllArgsConstructor;
import org.example.trackit.Mapper.EquipmentMapper;
import org.example.trackit.Mapper.JobMapper;
import org.example.trackit.Mapper.PartNumberMapper;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.Equipment;
import org.example.trackit.entity.properties.*;
import org.example.trackit.repository.EquipmentRepository;
import org.example.trackit.repository.JobRepository;
import org.example.trackit.services.EquipmentService;
import org.example.trackit.services.JobService;
import org.example.trackit.services.PartNumberService;
import org.example.trackit.exceptions.PartNumberNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class EquipmentServiceImpl implements EquipmentService<EquipmentDTO> {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;
    private final PartNumberService partNumberService;
    private final PartNumberMapper partNumberMapper;
    private final JobMapper jobMapper;
    private final JobRepository jobRepository;


    @Override
    public Page<EquipmentDTO> findAllEquipment
            (Map<String, String> filters, Pageable pageable) {
        if (filters.isEmpty()) return equipmentRepository.findAll(pageable).map(equipmentMapper::toDTO);

        Specification<Equipment> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, String> entry : filters.entrySet()) {
                if (entry.getKey().equalsIgnoreCase("partNumber")) {
                    predicates.add(criteriaBuilder.like(root.get(entry.getKey()), entry.getValue()));
                    continue;
                }
                if (entry.getKey().equalsIgnoreCase("serialNumber")) {
                    predicates.add(criteriaBuilder.like(root.get(entry.getKey()), entry.getValue()));
                    continue;
                }
                if (entry.getKey().equalsIgnoreCase("healthStatus")) {
                    predicates.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
                    continue;
                }
                if (entry.getKey().equalsIgnoreCase("allocationStatus")) {
                    predicates.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
                    continue;
                }
                if (entry.getKey().equalsIgnoreCase("jobName")) {
                    predicates.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
                }
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
    public EquipmentDTO update(int id, EquipmentDTO equipmentDTO) {
        Equipment existing = equipmentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        // Проверка, что можно изменять только location, если статус "On location"
        validateOnLocationStatus(existing, equipmentDTO);

        // Проверка, что Job обязателен  при смене статусе на "On location"
        validateJobForOnLocation(equipmentDTO);

        // Обработка смены статуса с "On location" на "On base" и обратно
        handleStatusChange(existing, equipmentDTO);

        Equipment updated = performUpdate(id, equipmentDTO);

        return equipmentMapper.toDTO(updated);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected Equipment performUpdate(int id, EquipmentDTO dto) {
        Equipment existing = equipmentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        existing.setSerialNumber(dto.getSerialNumber());
        PartNumber partNumber = partNumberMapper.toEntity(dto.getPartNumberDTO());
        existing.setPartNumber(partNumber);
        existing.setHealthStatus(dto.getHealthStatus());
        if (existing.getAllocationStatus() != dto.getAllocationStatus()) {
            existing.setAllocationStatus(dto.getAllocationStatus());
            existing.setAllocationStatusLastModified(LocalDateTime.now());
        }
        Optional<Job> optionalJob = jobRepository.findByJobName(dto.getJobName());
        if (optionalJob.isPresent()) {
            Job job = optionalJob.get();
            existing.setJob(job);
        }
        return equipmentRepository.save(existing);
    }

    @Override
    public EquipmentDTO findEquipmentById(int id) {
        return equipmentRepository.findById(id)
                .map(equipmentMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found"));
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

    private void validateOnLocationStatus(Equipment existing, EquipmentDTO dto) {
        if (existing.getAllocationStatus() == AllocationStatus.ON_LOCATION
                && dto.getAllocationStatus() != AllocationStatus.ON_BASE)
            throw new IllegalStateException("Cannot change state while equipment ON_LOCATION");
    }

    private void validateJobForOnLocation(EquipmentDTO dto) {
        if (dto.getAllocationStatus() == AllocationStatus.ON_LOCATION
                && dto.getJobName() == null) {
            throw new IllegalStateException("Job must be assigned when setting status to ON_LOCATION");
        }
    }

    private void handleStatusChange(Equipment existing, EquipmentDTO dto) {
        if (existing.getAllocationStatus() == AllocationStatus.ON_LOCATION
                && dto.getAllocationStatus() == AllocationStatus.ON_BASE) {
            dto.setHealthStatus(HealthStatus.RONG);
            dto.setJobName(null);
            dto.setAllocationStatusLastModified(LocalDateTime.now());
        } else if (existing.getAllocationStatus() == AllocationStatus.ON_BASE
                && dto.getAllocationStatus() == AllocationStatus.ON_LOCATION
                && dto.getHealthStatus() != HealthStatus.RITE) {
            throw new IllegalStateException("You can send to job only RITE equipment");
        }
    }
}

