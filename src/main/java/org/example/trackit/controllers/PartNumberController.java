package org.example.trackit.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trackit.dto.PartNumberDTO;
import org.example.trackit.services.PartNumberService;
import org.example.trackit.util.PartNumberValidator;
import org.example.trackit.util.exceptions.PartNumberAlreadyExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/partNumber")
@AllArgsConstructor
@Tag(name = "Part numbers API", description = "Operations related to part numbers management")
@Slf4j
public class PartNumberController {

    private final PartNumberService partNumberService;
    private final PartNumberValidator partNumberValidator;

    @GetMapping
    @Operation(summary = "Get a pageable list of all part numbers", description = "Get a pageable list of all part numbers with pagination")
    public ResponseEntity<Page<PartNumberDTO>> getAllPartNumbers(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "25") int size) {
        Page<PartNumberDTO> founded = partNumberService.findAllPartNumbers(PageRequest.of(page, size));
        return ResponseEntity.ok(founded);
    }

    @PostMapping
    @Operation(summary = "Create part number", description = "Creates a new part number with the provided details.")
    public ResponseEntity<PartNumberDTO> createPartNumber(
            @RequestBody PartNumberDTO partNumberDTO,
            BindingResult bindingResult) {
        partNumberValidator.validate(partNumberDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new PartNumberAlreadyExistException("Part number already exist: " + partNumberDTO.getNumber());
        }
        PartNumberDTO createdPN = partNumberService.save(partNumberDTO);
        return new ResponseEntity<>(createdPN, HttpStatus.CREATED);
    }

    @GetMapping("/{partNumber}")
    @Operation(summary = "Get info page about part number", description = "Get all information about part number")
    public ResponseEntity<PartNumberDTO> getPartNumber(@RequestParam String partNumber) {
        PartNumberDTO founded = partNumberService.getPartNumberDTOByPartNumber(partNumber);
        return ResponseEntity.ok(founded);
    }

    @PatchMapping
    @Operation(summary = "Update existing part number", description = "Update exciting part number")
    public ResponseEntity<PartNumberDTO> updatePartNumber(@RequestBody PartNumberDTO dto, BindingResult bindingResult) {
        partNumberValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) { //Part number is exciting
            PartNumberDTO updated = partNumberService.save(dto);
            return ResponseEntity.ok(updated);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    @Operation(summary = "Delete part number")
    public ResponseEntity<Void> deletePartNumber(@RequestParam String partNumber) {
        boolean deleted = partNumberService.deletePartNumber(partNumber);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
