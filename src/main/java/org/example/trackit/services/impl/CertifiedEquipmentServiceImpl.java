package org.example.trackit.services.impl;

import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.example.trackit.Mapper.CertifiedEquipmentMapper;
import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.entity.CertifiedEquipment;
import org.example.trackit.entity.properties.*;
import org.example.trackit.repository.CertifiedEquipmentRepository;
import org.example.trackit.repository.EquipmentRepository;
import org.example.trackit.services.EquipmentService;
import org.example.trackit.services.PartNumberService;
import org.example.trackit.exceptions.PartNumberNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final PartNumberService partNumberService;

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
        return founded.map(certifiedEquipmentMapper::toDTO).orElse(null);
    }

    @Override
    public boolean deleteEquipmentById(int id) {
        return false;
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

    @Override
    public CertifiedEquipmentDTO update(int id, CertifiedEquipmentDTO equipmentDTO) {
        return null;
    }
}
