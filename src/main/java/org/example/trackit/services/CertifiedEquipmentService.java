package org.example.trackit.services;

import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.entity.properties.AllocationStatus;
import org.example.trackit.entity.properties.CertificationStatus;
import org.example.trackit.entity.properties.HealthStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing certified equipment.
 */
public interface CertifiedEquipmentService {

    /**
     * Finds all certified equipment filtered by part number, serial number, health status,
     * allocation status, job name, and certification status, with pagination support.
     *
     * @param partNumber the part number of the equipment to search for, can be NULL
     * @param serialNumber the serial number of the equipment to search for, can be NULL
     * @param healthStatus the health status of the equipment, can be NULL
     * @param allocationStatus the allocation status of the equipment, can be NULL
     * @param jobName the job name associated with the equipment, can be NULL
     * @param certificationStatus the certification status of the equipment, can be NULL
     * @param pageable the pagination information, cannot be NULL
     * @return a page of certified equipment matching the search criteria
     */
    Page<CertifiedEquipmentDTO> findAllCertifiedEquipment(
            String partNumber, String serialNumber, HealthStatus healthStatus,
            AllocationStatus allocationStatus, String jobName,
            CertificationStatus certificationStatus, Pageable pageable
    );

    /**
     * Finds certified equipment by its ID.
     *
     * @param id the ID of the certified equipment to find
     * @return the certified equipment with the given ID
     */
    CertifiedEquipmentDTO findCertifiedEquipmentById(Integer id);

    /**
     * Saves the given certified equipment.
     *
     * @param dto the certified equipment to save
     * @return the saved certified equipment
     */
    CertifiedEquipmentDTO save(CertifiedEquipmentDTO dto);

    /**
     * Deletes certified equipment by its ID.
     *
     * @param id the ID of the certified equipment to delete
     * @return true if the equipment was deleted, false otherwise
     */
    boolean deleteById(int id);
}

