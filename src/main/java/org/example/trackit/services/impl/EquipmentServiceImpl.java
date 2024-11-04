package org.example.trackit.services.impl;

import ch.qos.logback.classic.Logger;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trackit.Mapper.EquipmentMapper;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.CertifiedEquipment;
import org.example.trackit.entity.Equipment;
import org.example.trackit.entity.properties.*;
import org.example.trackit.exceptions.JobNotFoundException;
import org.example.trackit.exceptions.PartNumberNotFoundException;
import org.example.trackit.logger.EquipmentLoggerFactory;
import org.example.trackit.repository.EquipmentRepository;
import org.example.trackit.repository.JobRepository;
import org.example.trackit.repository.PartNumberRepository;
import org.example.trackit.repository.specifications.EquipmentSpecifications;
import org.example.trackit.services.EquipmentService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EquipmentServiceImpl implements EquipmentService<EquipmentDTO> {

    private final EquipmentRepository equipmentRepository;
    private final PartNumberRepository partNumberRepository;
    private final JobRepository jobRepository;
    private final EquipmentMapper equipmentMapper;
    private final CertifiedEquipmentServiceImpl certifiedEquipmentServiceImpl;
    private final EquipmentLoggerFactory equipmentLoggerFactory;

    @Override
    //@Cacheable("equipmentList")
    public List<EquipmentDTO> findAll() {
        return equipmentRepository.findAll().stream().map(equipmentMapper::toDTO).toList();
    }

    @Override
    public Page<EquipmentDTO> findAllEquipment
            (Map<String, String> filters, Pageable pageable) {
        if (filters.isEmpty()) return equipmentRepository.findAll(pageable).map(equipmentMapper::toDTO);

        Specification<Equipment> spec = EquipmentSpecifications.filter(filters);
        Page<Equipment> page = equipmentRepository.findAll(spec, pageable);
        return page.map(equipmentMapper::toDTO);
    }

    @Override
    @Transactional
    public EquipmentDTO save(EquipmentDTO equipmentDTO) {
        PartNumber partNumber = partNumberRepository.findByNumber(equipmentDTO.getPartNumber())
                .orElseThrow(() -> new PartNumberNotFoundException(equipmentDTO.getPartNumber()));
        String serialNumber = equipmentDTO.getSerialNumber();
        Equipment equipment = new Equipment(partNumber, serialNumber);
        partNumber.getEquipmentList().add(equipment);
        Equipment saved = equipmentRepository.save(equipment);
        Logger logger = equipmentLoggerFactory.createLogger(partNumber.getNumber(), serialNumber);
        logger.info("Created new equipment with partNumber: {} and serialNumber: {}", partNumber.getNumber(), serialNumber);
        log.info("Created new equipment with partNumber: {} and serialNumber: {}", partNumber.getNumber(), serialNumber);
        return equipmentMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public EquipmentDTO update(int id, EquipmentDTO dto) {
        Equipment existing = equipmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found"));
        PartNumber partNumber = partNumberRepository.findByNumber(dto.getPartNumber())
                .orElseThrow(() -> new PartNumberNotFoundException("PartNumber not found"));
        Logger logger = equipmentLoggerFactory.createLogger(partNumber.getNumber(), dto.getSerialNumber());
        if (!existing.getSerialNumber().equalsIgnoreCase(dto.getSerialNumber())) {
            logger.info("Serial number updated from {} to {}", existing.getSerialNumber(), dto.getSerialNumber());
            existing.setSerialNumber(dto.getSerialNumber());
        }
        if (!existing.getPartNumber().getNumber().equalsIgnoreCase(dto.getPartNumber())) {
            logger.info("Partnumber updated from {} to {}", existing.getPartNumber().getNumber(), dto.getPartNumber());
            existing.setPartNumber(partNumber);
        }
        if (existing.getHealthStatus() != dto.getHealthStatus()) {
            logger.info("Health status updated from {} to {}", existing.getHealthStatus(), dto.getHealthStatus());
            existing.setHealthStatus(dto.getHealthStatus());
        }
        if (existing.getAllocationStatus() != dto.getAllocationStatus()) {
            logger.info("Allocation status updated from {} to {}", existing.getAllocationStatus(), dto.getAllocationStatus());
            maintainAllocationStatus(dto, existing);
        }
        existing.setComments(dto.getComments());
        if (dto.getJobName() != null && dto.getAllocationStatus() == AllocationStatus.ON_LOCATION) {
            Optional<Job> optionalJob = jobRepository.findByJobName(dto.getJobName());
            if (optionalJob.isPresent()) {
                Job job = optionalJob.get();
                logger.info("Equipment was send to job: {}", job.getJobName());
                existing.setJob(job);
                job.getEquipment().add(existing);
            } else throw new JobNotFoundException("Job not found");
        } else existing.setJob(null);
        Equipment updated = equipmentRepository.save(existing);
        partNumber.getEquipmentList().add(updated);
        log.info("Equipment {} : {} was successfully updated", dto.getPartNumber(), dto.getSerialNumber());
        return equipmentMapper.toDTO(updated);
    }

    @Override
    public EquipmentDTO findEquipmentById(int id) {
        Optional<Equipment> optional = equipmentRepository.findById(id);
        if (optional.isPresent()) {
            if (optional.get() instanceof CertifiedEquipment) {
                return certifiedEquipmentServiceImpl.findEquipmentById(id);
            }
        }
        return optional.map(equipmentMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found"));

    }

    @Override
    @Transactional
    public boolean deleteEquipmentById(int id) {
        Optional<Equipment> equipment = equipmentRepository.findById(id);
        if (equipment.isPresent()) {
            equipmentRepository.delete(equipment.get());
            return true;
        }
        return false;
    }
}

