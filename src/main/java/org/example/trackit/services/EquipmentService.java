package org.example.trackit.services;

import jakarta.validation.constraints.NotEmpty;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.Equipment;
import org.example.trackit.entity.properties.AllocationStatus;
import org.example.trackit.entity.properties.HealthStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service interface for managing equipment.
 */
public interface EquipmentService {

    /**
     * Finds all equipment filtered by part number, serial number, health status,
     * allocation status, and job name, with pagination support.
     *
     * @param partNumber the part number of the equipment to search for, can be NULL
     * @param serialNumber the serial number of the equipment to search for, can be NULL
     * @param healthStatus the health status of the equipment, can be NULL
     * @param allocationStatus the allocation status of the equipment, can be NULL
     * @param jobName the job name associated with the equipment, can be NULL
     * @param pageable the pagination information, cannot be NULL
     * @return a page of equipment matching the search criteria
     */
    Page<EquipmentDTO> findAllEquipment(
            String partNumber, String serialNumber, HealthStatus healthStatus,
            AllocationStatus allocationStatus, String jobName, Pageable pageable
    );

    /**
     * Saves the given equipment.
     *
     * @param equipmentDTO the equipment to save
     * @return the saved equipment
     */
    EquipmentDTO save(EquipmentDTO equipmentDTO);

    /**
     * Finds equipment by its ID.
     *
     * @param id the ID of the equipment to find
     * @return the equipment with the given ID
     */
    EquipmentDTO findEquipmentById(int id);

    /**
     * Finds equipment by part number and serial number.
     *
     * @param partNumber the part number of the equipment to find
     * @param serialNumber the serial number of the equipment to find
     * @return an optional containing the found equipment, or empty if not found
     */
    Optional<Equipment> findByPartNumberAndSerialNumber(@NotEmpty String partNumber, String serialNumber);

    /**
     * Deletes equipment by its ID.
     *
     * @param id the ID of the equipment to delete
     * @return true if the equipment was deleted, false otherwise
     */
    boolean deleteEquipmentById(int id);
}

