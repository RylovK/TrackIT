package org.example.trackit.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.example.trackit.Mapper.CertifiedEquipmentMapper;
import org.example.trackit.Mapper.PartNumberMapper;
import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.entity.CertifiedEquipment;
import org.example.trackit.entity.properties.*;
import org.example.trackit.exceptions.JobNotFoundException;
import org.example.trackit.repository.CertifiedEquipmentRepository;
import org.example.trackit.repository.EquipmentRepository;
import org.example.trackit.repository.JobRepository;
import org.example.trackit.services.EquipmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CertifiedEquipmentServiceImpl implements EquipmentService<CertifiedEquipmentDTO> {

    private final EquipmentRepository equipmentRepository;
    private final CertifiedEquipmentRepository certifiedEquipmentRepository;
    private final CertifiedEquipmentMapper certifiedEquipmentMapper;
    private final JobRepository jobRepository;
    private final PartNumberMapper partNumberMapper;

    @Override
    public Page<CertifiedEquipmentDTO> findAllEquipment
            (Map<String, String> filters, Pageable pageable) {
        Specification<CertifiedEquipment> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, String> entry : filters.entrySet()) {
                if (entry.getKey().equalsIgnoreCase("partNumber")) {
                    predicates.add(criteriaBuilder.like(root.get(entry.getKey()), entry.getValue()));
                    continue;
                }
                if (entry.getKey().equalsIgnoreCase("serialNumber")) {
                    predicates.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
                    continue;
                }
                if (entry.getKey().equalsIgnoreCase("healthStatus")) {
                    predicates.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
                    continue;
                }
                if (entry.getKey().equalsIgnoreCase("allocationStatus")) {
                    predicates.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
                }
                if (entry.getKey().equalsIgnoreCase("jobName")) {
                    predicates.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
                }
                if (entry.getKey().equalsIgnoreCase("certificationStatus")) {
                    predicates.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
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
        setEquipmentCertification(dto, equipment, partNumber);
        CertifiedEquipment saved = equipmentRepository.save(equipment);
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
        existing.setHealthStatus(dto.getHealthStatus());
        if (existing.getAllocationStatus() != dto.getAllocationStatus()) {
            existing.setAllocationStatus(dto.getAllocationStatus());
            existing.setAllocationStatusLastModified(LocalDateTime.now());
        }
        if (dto.getJobName() != null) {
            Optional<Job> optionalJob = jobRepository.findByJobName(dto.getJobName());
            if (optionalJob.isPresent()) {
                Job job = optionalJob.get();
                existing.setJob(job);
                job.getEquipment().add(existing);
            } else throw new JobNotFoundException("Job not found");
        } else existing.setJob(null);
        setEquipmentCertification(dto, existing, partNumber);
        return certifiedEquipmentMapper.toDTO(equipmentRepository.save(existing));
    }

    private void setEquipmentCertification(CertifiedEquipmentDTO dto, CertifiedEquipment existing, PartNumber partNumber) {
        if (dto.getCertificationDate() != null) {
            LocalDate certificationDate = dto.getCertificationDate();
            Period period = dto.getCertificationPeriod();
            LocalDate nextCertificationDate = certificationDate.plus(period);

            existing.setCertificationDate(certificationDate);
            existing.setCertificationPeriod(period);
            existing.setNextCertificationDate(nextCertificationDate);
            if (nextCertificationDate.isBefore(LocalDate.now())) {
                existing.setCertificationStatus(CertificationStatus.EXPIRED);
            } else {
                existing.setCertificationStatus(CertificationStatus.VALID);
            }
            existing.setFileCertificate(dto.getFileCertificate());
        }
        partNumber.getEquipmentList().add(existing);
    }

    @Override
    public boolean deleteEquipmentById(int id) {
        return false;
    }
}
