package org.example.trackit.repository;

import org.example.trackit.entity.CertifiedEquipment;
import org.example.trackit.entity.Equipment;
import org.example.trackit.entity.properties.PartNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {

    @Query("SELECT e FROM Equipment e JOIN e.partNumber pn WHERE pn.partNumber = :partNumber AND e.serialNumber = :serialNumber")
    Optional<Equipment> findByPartNumberAndSerialNumber(@Param("partNumber") String partNumber, @Param("serialNumber") String serialNumber);


    @Query("SELECT c FROM CertifiedEquipment c")
    List<CertifiedEquipment> findAllCertifiedEquipment();
}
