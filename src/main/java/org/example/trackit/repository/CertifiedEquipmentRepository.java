package org.example.trackit.repository;

import org.example.trackit.entity.CertifiedEquipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface CertifiedEquipmentRepository extends JpaRepository<CertifiedEquipment, Integer>, JpaSpecificationExecutor<CertifiedEquipment> {

    @NonNull
    Page<CertifiedEquipment> findAll(Specification<CertifiedEquipment> spec, @NonNull Pageable pageable);

    Optional<CertifiedEquipment> findCertifiedEquipmentById(Integer id);
}
