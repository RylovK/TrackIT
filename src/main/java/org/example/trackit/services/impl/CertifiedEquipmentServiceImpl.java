package org.example.trackit.services.impl;

import ch.qos.logback.classic.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trackit.Mapper.CertifiedEquipmentMapper;
import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.entity.CertifiedEquipment;
import org.example.trackit.entity.properties.*;
import org.example.trackit.exceptions.JobNotFoundException;
import org.example.trackit.exceptions.PartNumberNotFoundException;
import org.example.trackit.logger.EquipmentLoggerFactory;
import org.example.trackit.repository.CertifiedEquipmentRepository;
import org.example.trackit.repository.JobRepository;
import org.example.trackit.repository.PartNumberRepository;
import org.example.trackit.repository.specifications.EquipmentSpecifications;
import org.example.trackit.services.EquipmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CertifiedEquipmentServiceImpl implements EquipmentService<CertifiedEquipmentDTO> {

    private final CertifiedEquipmentRepository certifiedEquipmentRepository;
    private final PartNumberRepository partNumberRepository;
    private final JobRepository jobRepository;
    private final CertifiedEquipmentMapper certifiedEquipmentMapper;
    private final EquipmentLoggerFactory equipmentLoggerFactory;

    @Override
    public List<CertifiedEquipmentDTO> findAll() {
        return certifiedEquipmentRepository.findAll().stream().map(certifiedEquipmentMapper::toDTO).toList();
    }

    @Override
    public Page<CertifiedEquipmentDTO> findAllEquipment
            (Map<String, String> filters, Pageable pageable) {
        Specification<CertifiedEquipment> spec = EquipmentSpecifications.filter(filters);
        Page<CertifiedEquipment> page = certifiedEquipmentRepository.findAll(spec, pageable);
        return page.map(certifiedEquipmentMapper::toDTO);
    }

    @Override
    public CertifiedEquipmentDTO findEquipmentById(int id) {
        Optional<CertifiedEquipment> founded = certifiedEquipmentRepository.findCertifiedEquipmentById(id);
        return founded.map(certifiedEquipmentMapper::toDTO).orElseThrow(() -> new EntityNotFoundException("Equipment not found"));
    }

    @Override
    @Transactional
    public CertifiedEquipmentDTO save(CertifiedEquipmentDTO dto) {
        PartNumber partNumber = partNumberRepository.findByNumber(dto.getPartNumber())
                .orElseThrow(() -> new PartNumberNotFoundException("Part number not found"));
        CertifiedEquipment equipment = new CertifiedEquipment(partNumber, dto.getSerialNumber());
        Logger logger = equipmentLoggerFactory.getLogger(partNumber.getNumber(), equipment.getSerialNumber());
        //setEquipmentCertification(dto, equipment);
        partNumber.getEquipmentList().add(equipment);
        CertifiedEquipment saved = certifiedEquipmentRepository.save(equipment);
        logger.info("Equipment {}: {} was successfully created", saved.getPartNumber().getNumber(), saved.getSerialNumber());
        log.info("Equipment {}: {} was successfully created", saved.getPartNumber().getNumber(), saved.getSerialNumber());
        return certifiedEquipmentMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public CertifiedEquipmentDTO update(int id, CertifiedEquipmentDTO dto) {
        CertifiedEquipment existing = certifiedEquipmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found"));
        PartNumber partNumber = partNumberRepository.findByNumber(dto.getPartNumber())
                .orElseThrow(() -> new PartNumberNotFoundException("Part number not found"));
        Logger logger = equipmentLoggerFactory.getLogger(partNumber.getNumber(), dto.getSerialNumber());
        updateEquipmentFields(dto, existing, partNumber, logger);
        if (dto.getJobName() != null && dto.getAllocationStatus() == AllocationStatus.ON_LOCATION) {
            jobRepository.findByJobName(dto.getJobName()).ifPresentOrElse(job -> updateJob(job, logger, existing),
                    () -> {
                        throw new JobNotFoundException("Job not found");
                    });
        } else existing.setJob(null);
        setEquipmentCertification(dto, existing, logger);
        partNumber.getEquipmentList().add(existing);
        log.info("Equipment {}: {} was successfully updated", dto.getPartNumber(), dto.getSerialNumber());
        return certifiedEquipmentMapper.toDTO(certifiedEquipmentRepository.save(existing));
    }

    @Override
    @Transactional
    public boolean deleteEquipmentById(int id) {
        Optional<CertifiedEquipment> equipment = certifiedEquipmentRepository.findById(id);
        if (equipment.isPresent()) {
            CertifiedEquipment founded = equipment.get();
            Logger logger = equipmentLoggerFactory.getLogger(founded.getPartNumber().getNumber(), founded.getSerialNumber());
            logger.info("Equipment {}: {} was successfully deleted", founded.getSerialNumber(), founded.getPartNumber().getNumber());
            log.info("Equipment {}: {} was successfully deleted", founded.getSerialNumber(), founded.getPartNumber().getNumber());
            certifiedEquipmentRepository.delete(equipment.get());
            return true;
        }
        return false;
    }

    private void setEquipmentCertification(CertifiedEquipmentDTO dto, CertifiedEquipment equipment, Logger logger) {
        LocalDate certificationDate = dto.getCertificationDate();
        int certificationPeriod = dto.getCertificationPeriod();
        LocalDate nextCertificationDate = certificationDate.plusMonths(certificationPeriod);
        if (!certificationDate.equals(equipment.getCertificationDate())) {
            equipment.setCertificationDate(certificationDate);
            equipment.setCertificationPeriod(certificationPeriod);
            equipment.setNextCertificationDate(nextCertificationDate);
            logger.info("Certification date was updated. Next certification date: {}", nextCertificationDate);
            log.info("Certification date was updated for {}: {}", equipment.getPartNumber().getNumber(), dto.getSerialNumber());
        }
        if (certificationPeriod != equipment.getCertificationPeriod()) {
            equipment.setCertificationPeriod(certificationPeriod);
            equipment.setNextCertificationDate(nextCertificationDate);
            logger.info("Certification period was updated = {} months. Next certification date: {}", certificationPeriod, nextCertificationDate);
            log.info("Certification date was updated for {}: {}", equipment.getPartNumber().getNumber(), dto.getSerialNumber());
        }
        equipment.setFileCertificate(dto.getFileCertificate());

        updateCertificationStatus(equipment);
    }

    private void updateCertificationStatus(CertifiedEquipment equipment) {
        LocalDate now = LocalDate.now();
        if (now.isBefore(equipment.getNextCertificationDate())) {
            equipment.setCertificationStatus(CertificationStatus.VALID);
        } else {
            equipment.setCertificationStatus(CertificationStatus.EXPIRED);
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    @PostConstruct
    @Transactional
    protected void scheduledUpdateCertificationStatus() {
        List<CertifiedEquipment> all = certifiedEquipmentRepository.findAll();
        for (CertifiedEquipment equipment : all) {
            updateCertificationStatus(equipment);
        }
        certifiedEquipmentRepository.saveAll(all);
        log.info("Scheduled update of certification status completed");
    }
}
