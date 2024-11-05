package org.example.trackit.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.dto.CreateCertifiedEquipmentDTO;
import org.example.trackit.exceptions.ValidationErrorException;
import org.example.trackit.services.EquipmentService;
import org.example.trackit.services.FileService;
import org.example.trackit.validators.EquipmentValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/certified")
@RequiredArgsConstructor
@Tag(name = "Certified equipment API", description = "Operations related to equipment management and its certification")
public class CertifiedEquipmentController {

    private final EquipmentService<CertifiedEquipmentDTO> equipmentService;
    private final EquipmentValidator equipmentValidator;
    private final FileService fileService;

    @GetMapping
    @Operation(summary = "Find all certified equipment", description = "Get a list of all certified equipment with filtration and pagination")
    public ResponseEntity<Page<CertifiedEquipmentDTO>> getAllCertifiedEquipment(@RequestParam(defaultValue = "0") int page,
                                                                                @RequestParam(defaultValue = "25") int size,
                                                                                @RequestParam(required = false) Map<String, String> filters,
                                                                                @RequestParam(defaultValue = "nextCertificationDate") String sortBy,
                                                                                @RequestParam(defaultValue = "asc") String sortDirection) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
        Page<CertifiedEquipmentDTO> allCertifiedEquipment = equipmentService.findAllEquipment(filters, pageRequest);
        return new ResponseEntity<>(allCertifiedEquipment, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CertifiedEquipmentDTO>> getAllCertifiedEquipment() {
        return ResponseEntity.ok(equipmentService.findAll());
    }

    @PostMapping
    @Operation(summary = "Create certified equipment")
    public ResponseEntity<CertifiedEquipmentDTO> createEquipment(@RequestBody @Valid CreateCertifiedEquipmentDTO equipmentDTO, BindingResult bindingResult) {
        equipmentValidator.validate(equipmentDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationErrorException(bindingResult);
        }
        CertifiedEquipmentDTO createdEquipment = equipmentService.save(new CertifiedEquipmentDTO(equipmentDTO));
        return new ResponseEntity<>(createdEquipment, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update certified equipment")
    public ResponseEntity<CertifiedEquipmentDTO> updateEquipment(@PathVariable int id, @RequestBody @Valid CertifiedEquipmentDTO equipmentDTO, BindingResult bindingResult) {
        equipmentValidator.validateUpdate(id,equipmentDTO, bindingResult);
        equipmentValidator.validateCertification(id, equipmentDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationErrorException(bindingResult);
        }
        CertifiedEquipmentDTO updated = equipmentService.update(id, equipmentDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PatchMapping("/{id}/upload")
    @Operation(summary = "Upload a certificate file")
    public ResponseEntity<String> uploadCertificate(@PathVariable int id,
                                                    @RequestParam("file") MultipartFile file) {
        String fileUrl = fileService.saveFile(id, file);
        return new ResponseEntity<>(fileUrl, HttpStatus.OK);
    }
}
