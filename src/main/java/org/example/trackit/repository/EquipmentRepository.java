package org.example.trackit.repository;

import org.example.trackit.entity.parts.Equipment;
import org.example.trackit.entity.parts.PartNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {

    Optional<Equipment> findByPartNumberAndSerialNumber(PartNumber partNumber, String serialNumber);
}
