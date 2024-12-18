package org.example.trackit.services;

import org.example.trackit.dto.PartNumberDTO;
import org.example.trackit.entity.properties.PartNumber;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing part numbers.
 */
@Transactional(readOnly = true)
public interface PartNumberService {

    /**
     * Retrieves all part numbers
     *
     * @return list of part numbers
     */
    List<PartNumberDTO> findAllPartNumbers();


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
    @Transactional
    PartNumberDTO save(PartNumberDTO partNumberDTO);

    /**
     *
     * @param excitingPartNumber Part number to be updated
     * @param partNumberDTO DTO with updated fields
     * @return Updated DTO
     */
    @Transactional
    PartNumberDTO update(String excitingPartNumber, PartNumberDTO partNumberDTO);

    /**
     * Deletes a part number by its number.
     *
     * @param number the part number to delete
     * @return true if the part number was deleted, false otherwise
     */
    @Transactional
    boolean deletePartNumber(String number);
}
