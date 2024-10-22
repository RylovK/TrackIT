package org.example.trackit.services;

import org.example.trackit.dto.EquipmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Service interface for managing equipment.
 */
public interface EquipmentService<T extends EquipmentDTO> {

    /**
     *
     * @return list of all equipment
     */
    List<T> findAll();

    /**
     *
     * @param pageable for pagination information, cannot be NULL
     * @param filters map of filters for part number, serial number, health status,
     *  *                allocation status etc., each filter can be NULL
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
     *
     * @param id of equipment need to be updated
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

    int convertIfNeed(int id);

    String saveFile(int id, MultipartFile file);
}

