package org.example.trackit.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trackit.dto.PartNumberDTO;
import org.example.trackit.exceptions.ValidationErrorException;
import org.example.trackit.services.PartNumberService;
import org.example.trackit.services.impl.FileService;
import org.example.trackit.validators.PartNumberValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/partNumber")
@RequiredArgsConstructor
@Tag(name = "Part numbers API", description = "Operations related to part numbers management")
public class PartNumberController {

    private final PartNumberService partNumberService;
    private final PartNumberValidator partNumberValidator;
    private final FileService fileService;

    @GetMapping
    @Operation(summary = "Get a list of all part numbers")
    public ResponseEntity<List<PartNumberDTO>> getAllPartNumbers() {
        List<PartNumberDTO> founded = partNumberService.findAllPartNumbers();
        return ResponseEntity.ok(founded);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @Operation(summary = "Create part number", description = "Creates a new part number with the provided details.")
    public ResponseEntity<PartNumberDTO> createPartNumber(
            @RequestBody @Valid PartNumberDTO partNumberDTO,
            BindingResult bindingResult) {
        partNumberValidator.validate(partNumberDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationErrorException(bindingResult);
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{partNumber}")
    @Operation(summary = "Update existing part number", description = "Update exciting part number")
    public ResponseEntity<PartNumberDTO> updatePartNumber(@PathVariable String partNumber,
                                                          @RequestBody @Valid PartNumberDTO dto,
                                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationErrorException(bindingResult);
        }
        PartNumberDTO updated = partNumberService.update(partNumber, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PatchMapping("/{partNumber}/upload")
    @Operation(summary = "Upload image for part number", description = "Upload an image for an existing part number")
    public ResponseEntity<String> uploadImage(@PathVariable String partNumber,
                                              @RequestParam("file") MultipartFile file) {
        String fileUrl = fileService.saveImage(partNumber, file);
        return new ResponseEntity<>(fileUrl, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
