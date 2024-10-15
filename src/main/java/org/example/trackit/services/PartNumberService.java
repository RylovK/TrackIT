package org.example.trackit.services;

import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.dto.PartNumberDTO;
import org.example.trackit.entity.properties.PartNumber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing part numbers.
 */
public interface PartNumberService {

    /**
     * Retrieves all part numbers with pagination
     *
     * @param pageable for pagination information, cannot be NULL
     * @return page of part numbers
     */
    Page<PartNumberDTO> findAllPartNumbers(Pageable pageable);

//    /**
//     *
//     * @param partNumber for searching equipment list
//     * @param pageable for pagination information, cannot be NULL
//     * @return page of equipment with provided part number
//     */
//    Page<EquipmentDTO> findAllEquipmentByPartNumber(String partNumber, Pageable pageable);

    /**
     * Getting PartNumberDTO by its number. Used for controller
     *
     * @param partNumber the part number to find
     * @return DTO of found part number
     */
    PartNumberDTO getPartNumberDTOByPartNumber(String partNumber);


    /**
     * Finds a part number by its number. Used for validation
     *
     * @param number the part number to find
     * @return an optional containing the found part number, or empty if not found
     */
    Optional<PartNumber> findPartNumberByNumber(String number);

    /**
     * Saves the given part number.
     *
     * @param partNumberDTO the part number data to save
     * @return the saved part number
     */
    PartNumberDTO save(PartNumberDTO partNumberDTO);

    /**
     * Deletes a part number by its number.
     *
     * @param number the part number to delete
     * @return true if the part number was deleted, false otherwise
     */
    boolean deletePartNumber(String number);
}
