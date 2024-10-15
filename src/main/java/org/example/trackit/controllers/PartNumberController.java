package org.example.trackit.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.trackit.dto.PartNumberDTO;
import org.example.trackit.services.PartNumberService;
import org.example.trackit.validators.PartNumberValidator;
import org.example.trackit.exceptions.PartNumberAlreadyExistException;
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
            @RequestBody @Valid PartNumberDTO partNumberDTO,
            BindingResult bindingResult) {
        partNumberValidator.validate(partNumberDTO, bindingResult);
        if (bindingResult.hasErrors()) {//TODO: валидация на работает
            throw new PartNumberAlreadyExistException("Part number already exist: " + partNumberDTO.getNumber());
        }
        PartNumberDTO createdPN = partNumberService.save(partNumberDTO);
        return new ResponseEntity<>(createdPN, HttpStatus.CREATED);
    }

    @GetMapping("/{partNumber}")
    @Operation(summary = "Get info page about part number", description = "Get all information about part number")
    public ResponseEntity<PartNumberDTO> getPartNumber(@PathVariable String partNumber) {
        PartNumberDTO founded = partNumberService.getPartNumberDTOByPartNumber(partNumber);
        return ResponseEntity.ok(founded);
    }

    @PatchMapping("/{partNumber}")
    @Operation(summary = "Update existing part number", description = "Update exciting part number")
    public ResponseEntity<PartNumberDTO> updatePartNumber(@PathVariable String partNumber,
                                                          @RequestBody PartNumberDTO dto,
                                                          BindingResult bindingResult) {
//        partNumberValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);} //TODO: настроить валидацию и ответ
        PartNumberDTO updated = partNumberService.update(partNumber, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
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
