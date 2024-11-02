package org.example.trackit.services;

import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.Equipment;
import org.example.trackit.entity.properties.AllocationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    T save(T equipmentDTO);

    /**
     * @param id           of equipment need to be updated
     * @param equipmentDTO DTO with updated fields
     * @return updated DTO
     */
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
    boolean deleteEquipmentById(int id);

    default void maintainAllocationStatus(EquipmentDTO dto, Equipment existing) {
        if (existing.getAllocationStatus() != dto.getAllocationStatus()) {
            if (existing.getAllocationStatus() == AllocationStatus.ON_BASE
                    && dto.getAllocationStatus() == AllocationStatus.ON_LOCATION) {
                existing.setLastJob("Shipped to: " + dto.getJobName() + " on " + LocalDate.now());
            } else {
                existing.setLastJob("Returned from: " + dto.getJobName() + " on " + LocalDate.now());
            }
            existing.setAllocationStatus(dto.getAllocationStatus());
            existing.setAllocationStatusLastModified(LocalDate.now());
        }
    }
}

