package org.example.trackit.repository;

import org.example.trackit.entity.CertifiedEquipment;
import org.example.trackit.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Integer>, JpaSpecificationExecutor<Equipment> {

    @Query("SELECT e FROM Equipment e WHERE e.partNumber.number = :partNumber AND e.serialNumber = :serialNumber")
    Optional<Equipment> findByPartNumberAndSerialNumber(@Param("partNumber") String partNumber, @Param("serialNumber") String serialNumber);

    Page<CertifiedEquipment> findAllCertifiedEquipment(Specification<CertifiedEquipment> spec, Pageable pageable);
}
