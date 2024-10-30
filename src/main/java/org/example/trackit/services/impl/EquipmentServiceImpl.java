package org.example.trackit.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.trackit.Mapper.CertifiedEquipmentMapper;
import org.example.trackit.Mapper.EquipmentMapper;
import org.example.trackit.Mapper.PartNumberMapper;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.CertifiedEquipment;
import org.example.trackit.entity.Equipment;
import org.example.trackit.entity.properties.*;
import org.example.trackit.exceptions.JobNotFoundException;
import org.example.trackit.exceptions.PartNumberNotFoundException;
import org.example.trackit.repository.CertifiedEquipmentRepository;
import org.example.trackit.repository.EquipmentRepository;
import org.example.trackit.repository.JobRepository;
import org.example.trackit.repository.PartNumberRepository;
import org.example.trackit.services.EquipmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipmentServiceImpl implements EquipmentService<EquipmentDTO> {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;
    private final PartNumberMapper partNumberMapper;
    private final JobRepository jobRepository;
    private final CertifiedEquipmentMapper certifiedEquipmentMapper;
    private final CertifiedEquipmentRepository certifiedEquipmentRepository;
    private final PartNumberRepository partNumberRepository;

    @Override
    public List<EquipmentDTO> findAll() {
        return equipmentRepository.findAll().stream().map(equipmentMapper::toDTO).toList();
    }

    @Override
    public Page<EquipmentDTO> findAllEquipment
            (Map<String, String> filters, Pageable pageable) {
        if (filters.isEmpty()) return equipmentRepository.findAll(pageable).map(equipmentMapper::toDTO);

        Specification<Equipment> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, String> entry : filters.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toLowerCase();

                if (key.equalsIgnoreCase("partNumber")) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(key)), "%" + value + "%"));
                    continue;
                }
                if (key.equalsIgnoreCase("serialNumber")) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(key)), "%" + value + "%"));
                    continue;
                }
                if (key.equalsIgnoreCase("healthStatus")) {
                    HealthStatus status = HealthStatus.valueOf(value.toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get(key), status));
                    continue;
                }
                if (key.equalsIgnoreCase("allocationStatus")) {
                    AllocationStatus status = AllocationStatus.valueOf(value.toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get(key), status));
                    continue;
                }
                if (key.equalsIgnoreCase("jobName")) {
                    predicates.add(criteriaBuilder.equal(
                            criteriaBuilder.lower(root.get("job").get("jobName")), value));
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
        PartNumber partNumber = partNumberMapper.toEntity(equipmentDTO.getPartNumberDTO());
        Equipment equipment = new Equipment(partNumber, equipmentDTO.getSerialNumber());
        partNumber.getEquipmentList().add(equipment);
        equipmentRepository.save(equipment);
        return equipmentMapper.toDTO(equipment);
    }

    @Override
    @Transactional
    public EquipmentDTO update(int id, EquipmentDTO dto) {
        Equipment existing = equipmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found"));
        existing.setSerialNumber(dto.getSerialNumber());
        PartNumber partNumber = partNumberRepository.findByNumber(dto.getPartNumber())
                .orElseThrow(() -> new PartNumberNotFoundException("PartNumber not found"));
        existing.setPartNumber(partNumber);
        existing.setHealthStatus(dto.getHealthStatus());
        if (existing.getAllocationStatus() != dto.getAllocationStatus()) {
            if (existing.getAllocationStatus() == AllocationStatus.ON_BASE
            && dto.getAllocationStatus() == AllocationStatus.ON_LOCATION) {
                existing.setLastJob("Shipped to: " + dto.getJobName() + " on " + LocalDate.now());
            }
            existing.setAllocationStatus(dto.getAllocationStatus());
            existing.setAllocationStatusLastModified(LocalDate.now());
        }
        if (dto.getJobName() != null && dto.getAllocationStatus() == AllocationStatus.ON_LOCATION) {
            Optional<Job> optionalJob = jobRepository.findByJobName(dto.getJobName());
            if (optionalJob.isPresent()) {
                Job job = optionalJob.get();
                existing.setJob(job);
                job.getEquipment().add(existing);
            } else throw new JobNotFoundException("Job not found");
        } else existing.setJob(null);
        existing.setComments(dto.getComments());
        Equipment saved = equipmentRepository.save(existing);
        partNumber.getEquipmentList().add(saved);
        return equipmentMapper.toDTO(saved);
    }

    @Override
    public EquipmentDTO findEquipmentById(int id) {
        Optional<Equipment> optional = equipmentRepository.findById(id);
        if (optional.isPresent()) {
            if (optional.get() instanceof CertifiedEquipment) {
                Optional<CertifiedEquipment> byId = certifiedEquipmentRepository.findCertifiedEquipmentById(id);
                return byId.map(certifiedEquipmentMapper::toDTO).orElseThrow(() -> new EntityNotFoundException("Equipment not found"));
            }
        }
        return optional.map(equipmentMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found"));

    }

    @Override
    @Transactional
    public boolean deleteEquipmentById(int id) {
        Optional<Equipment> byId = equipmentRepository.findById(id);
        System.out.println("Founded " + byId.isPresent() + "id: " + id);
        if (byId.isPresent()) {
            equipmentRepository.delete(byId.get());
            return true;
        }
        return false;
    }
}

