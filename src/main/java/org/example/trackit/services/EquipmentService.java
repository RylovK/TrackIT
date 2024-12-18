package org.example.trackit.services;

import ch.qos.logback.classic.Logger;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.Equipment;
import org.example.trackit.entity.properties.AllocationStatus;
import org.example.trackit.entity.properties.HealthStatus;
import org.example.trackit.entity.properties.Job;
import org.example.trackit.entity.properties.PartNumber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service interface for managing equipment.
 */
public interface EquipmentService<T extends EquipmentDTO> {

    /**
     * @return list of all equipment
     */
    List<T> findAll();

    /**
     * @param pageable for pagination information, cannot be NULL
     * @param filters  map of filters for part number, serial number, health status,
     *                 *                allocation status etc., each filter can be NULL
     * @return a page of equipment matching the search criteria
     */
    Page<T> findAllEquipment(Map<String, String> filters,
                             Pageable pageable);

    /**
     * Saves the given equipment.
     *
     * @param equipmentDTO the equipment to save
     * @return the saved equipment
     */
    @Transactional
    T save(T equipmentDTO);

    /**
     * @param id           of equipment need to be updated
     * @param equipmentDTO DTO with updated fields
     * @return updated DTO
     */
    @Transactional
    T update(int id, T equipmentDTO);

    /**
     * Finds equipment by its ID.
     *
     * @param id the ID of the equipment to find
     * @return the equipment with the given ID
     */
    T findEquipmentById(int id);

    /**
     * Deletes equipment by its ID.
     *
     * @param id the ID of the equipment to delete
     * @return true if the equipment was deleted, false otherwise
     */
    @Transactional
    boolean deleteEquipmentById(int id);

    default void updateEquipmentFields(EquipmentDTO dto, Equipment existing, Logger logger) {
        updateSerialAndPartNumber(dto, existing, logger);
        updateEquipmentStatus(dto, existing, logger);
        existing.setComments(dto.getComments());
    }

    private void updateEquipmentStatus(EquipmentDTO dto, Equipment existing, Logger logger) {
        if (existing.getAllocationStatus() != dto.getAllocationStatus()) {
            if (existing.getAllocationStatus() == AllocationStatus.ON_BASE
                    && dto.getAllocationStatus() == AllocationStatus.ON_LOCATION) {
                existing.setLastJob(dto.getJobName());
            } else if (existing.getAllocationStatus() == AllocationStatus.ON_LOCATION
                    && dto.getAllocationStatus() == AllocationStatus.ON_BASE) {
                dto.setHealthStatus(HealthStatus.RONG);
                logger.info("Health status updated from {} to {}", existing.getHealthStatus(), dto.getHealthStatus());
                existing.setHealthStatus(HealthStatus.RONG);
                existing.setJob(null);
            }
            logger.info("Allocation status updated from {} to {}", existing.getAllocationStatus(), dto.getAllocationStatus());
            existing.setAllocationStatus(dto.getAllocationStatus());
            existing.setAllocationStatusLastModified(LocalDate.now());
        }
        if (existing.getHealthStatus() != dto.getHealthStatus()) {
            logger.info("Health status updated from {} to {}", existing.getHealthStatus(), dto.getHealthStatus());
            existing.setHealthStatus(dto.getHealthStatus());
        }
    }

    private void updateSerialAndPartNumber(EquipmentDTO dto, Equipment existing, Logger logger) {
        if (!existing.getSerialNumber().equalsIgnoreCase(dto.getSerialNumber())) {
            logger.info("Serial number updated from {} to {}", existing.getSerialNumber(), dto.getSerialNumber());
            existing.setSerialNumber(dto.getSerialNumber());
        }
        if (!existing.getPartNumber().getNumber().equalsIgnoreCase(dto.getPartNumber())) {
            throw new UnsupportedOperationException("Part number cannot be changed");
        }
    }

    default void updateJob(Job job, Logger logger, Equipment existing) {
        logger.info("Equipment was send to job: {}", job.getJobName());
        existing.setJob(job);
        job.getEquipment().add(existing);
    }
}

