package org.example.trackit.services.impl;

import ch.qos.logback.classic.Logger;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trackit.Mapper.EquipmentMapper;
import org.example.trackit.dto.CertifiedEquipmentDTO;
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
    private final EquipmentService<CertifiedEquipmentDTO> certifiedEquipmentServiceImpl;
    private final EquipmentLoggerFactory equipmentLoggerFactory;

    @Override
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
    public EquipmentDTO save(EquipmentDTO equipmentDTO) {
        PartNumber partNumber = partNumberRepository.findByNumber(equipmentDTO.getPartNumber())
                .orElseThrow(() -> new PartNumberNotFoundException(equipmentDTO.getPartNumber()));
        String serialNumber = equipmentDTO.getSerialNumber();
        Equipment equipment = new Equipment(partNumber, serialNumber);
        partNumber.getEquipmentList().add(equipment);
        Equipment saved = equipmentRepository.save(equipment);
        Logger logger = equipmentLoggerFactory.getLogger(partNumber.getNumber(), serialNumber);
        logger.info("Equipment {}: {} was successfully created", saved.getPartNumber().getNumber(), saved.getSerialNumber());
        log.info("Equipment {}: {} was successfully created", saved.getPartNumber().getNumber(), saved.getSerialNumber());
        return equipmentMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public EquipmentDTO update(int id, EquipmentDTO dto) {
        Equipment existing = equipmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found"));
        PartNumber partNumber = partNumberRepository.findByNumber(dto.getPartNumber())
                .orElseThrow(() -> new PartNumberNotFoundException("PartNumber not found"));
        Logger logger = equipmentLoggerFactory.getLogger(partNumber.getNumber(), dto.getSerialNumber());
        updateEquipmentFields(dto, existing, partNumber, logger);
        if (dto.getJobName() != null && dto.getAllocationStatus() == AllocationStatus.ON_LOCATION) {
            jobRepository.findByJobName(dto.getJobName())
                    .ifPresentOrElse(job -> updateJob(job, logger, existing),
                            () -> {
                                throw new JobNotFoundException("Job not found");
                            });
        } else existing.setJob(null);
        Equipment updated = equipmentRepository.save(existing);

        log.info("Equipment {}: {} was successfully updated", dto.getPartNumber(), dto.getSerialNumber());
        return equipmentMapper.toDTO(updated);
    }


    @Override
    @Transactional
    public boolean deleteEquipmentById(int id) {
        Optional<Equipment> equipment = equipmentRepository.findById(id);
        if (equipment.isPresent()) {
            Equipment founded = equipment.get();
            Logger logger = equipmentLoggerFactory.getLogger(founded.getPartNumber().getNumber(), founded.getSerialNumber());
            logger.info("Equipment {}: {} was successfully deleted", founded.getSerialNumber(), founded.getPartNumber().getNumber());
            log.info("Equipment {}: {} was successfully deleted", founded.getSerialNumber(), founded.getPartNumber().getNumber());
            equipmentRepository.delete(equipment.get());
            return true;
        }
        return false;
    }
}

