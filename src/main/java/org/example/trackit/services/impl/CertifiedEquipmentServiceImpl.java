package org.example.trackit.services.impl;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.trackit.Mapper.CertifiedEquipmentMapper;
import org.example.trackit.Mapper.PartNumberMapper;
import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.entity.CertifiedEquipment;
import org.example.trackit.entity.properties.*;
import org.example.trackit.exceptions.JobNotFoundException;
import org.example.trackit.repository.CertifiedEquipmentRepository;
import org.example.trackit.repository.JobRepository;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CertifiedEquipmentServiceImpl implements EquipmentService<CertifiedEquipmentDTO> {

    private final CertifiedEquipmentRepository certifiedEquipmentRepository;
    private final JobRepository jobRepository;
    private final CertifiedEquipmentMapper certifiedEquipmentMapper;
    private final PartNumberMapper partNumberMapper;

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
        PartNumber partNumber = partNumberMapper.toEntity(dto.getPartNumberDTO());
        CertifiedEquipment equipment = new CertifiedEquipment(partNumber, dto.getSerialNumber());
        //setEquipmentCertification(dto, equipment);
        partNumber.getEquipmentList().add(equipment);
        CertifiedEquipment saved = certifiedEquipmentRepository.save(equipment);
        return certifiedEquipmentMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public CertifiedEquipmentDTO update(int id, CertifiedEquipmentDTO dto) {
        CertifiedEquipment existing = certifiedEquipmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found"));
        existing.setSerialNumber(dto.getSerialNumber());
        PartNumber partNumber = partNumberMapper.toEntity(dto.getPartNumberDTO());
        existing.setPartNumber(partNumber);
        existing.setHealthStatus(dto.getHealthStatus() == null ? HealthStatus.RONG : dto.getHealthStatus());
        maintainAllocationStatus(dto, existing);
        if (dto.getJobName() != null) {
            Optional<Job> optionalJob = jobRepository.findByJobName(dto.getJobName());
            if (optionalJob.isPresent()) {
                Job job = optionalJob.get();
                existing.setJob(job);
                job.getEquipment().add(existing);
            } else throw new JobNotFoundException("Job not found");
        } else existing.setJob(null);
        existing.setComments(dto.getComments());
        setEquipmentCertification(dto, existing);
        partNumber.getEquipmentList().add(existing);

        return certifiedEquipmentMapper.toDTO(certifiedEquipmentRepository.save(existing));
    }

    @Override
    @Transactional
    public boolean deleteEquipmentById(int id) {
        Optional<CertifiedEquipment> equipment = certifiedEquipmentRepository.findById(id);
        if (equipment.isPresent()) {
            certifiedEquipmentRepository.delete(equipment.get());
            return true;
        }
        return false;
    }

    private void setEquipmentCertification(CertifiedEquipmentDTO dto, CertifiedEquipment equipment) {
        LocalDate certificationDate = dto.getCertificationDate();
        int certificationPeriod = dto.getCertificationPeriod();
        LocalDate nextCertificationDate = certificationDate.plusMonths(certificationPeriod);

        equipment.setCertificationDate(certificationDate);
        equipment.setCertificationPeriod(certificationPeriod);
        equipment.setFileCertificate(dto.getFileCertificate());
        equipment.setNextCertificationDate(nextCertificationDate);

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
    }
}
