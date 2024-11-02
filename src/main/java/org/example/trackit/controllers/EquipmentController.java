package org.example.trackit.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.dto.CreateEquipmentDTO;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.exceptions.ValidationErrorException;
import org.example.trackit.services.EquipmentService;
import org.example.trackit.validators.EquipmentValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/equipment")
@RequiredArgsConstructor
@Tag(name = "Equipment API", description = "Operations related to equipment management")
public class EquipmentController {

    private final EquipmentService<EquipmentDTO> equipmentService;
    private final EquipmentValidator equipmentValidator;

    @GetMapping
    @Operation(summary = "Find all equipment", description = "Get a list of all equipment with filtration and pagination")
    public ResponseEntity<Page<EquipmentDTO>> getAllEquipment(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "25") int size,
                                                              @RequestParam(required = false) Map<String, String> filters) {
        Page<EquipmentDTO> dtoPage = equipmentService.findAllEquipment(filters, PageRequest.of(page, size));
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentDTO> getEquipmentById(@PathVariable int id) {
        EquipmentDTO equipmentDTO = equipmentService.findEquipmentById(id);
        if (equipmentDTO instanceof CertifiedEquipmentDTO certifiedEquipmentDTO) {
            return ResponseEntity.ok(certifiedEquipmentDTO);
        }
        return new ResponseEntity<>(equipmentDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EquipmentDTO> createEquipment(@RequestBody @Valid CreateEquipmentDTO createEquipmentDTO, BindingResult bindingResult) {
        equipmentValidator.validate(createEquipmentDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationErrorException(bindingResult);
        }
        EquipmentDTO createdEquipment = equipmentService.save(new EquipmentDTO(createEquipmentDTO));
        return new ResponseEntity<>(createdEquipment, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EquipmentDTO> updateEquipment(@PathVariable int id,
                                                        @RequestBody @Valid EquipmentDTO equipmentDTO,
                                                        BindingResult bindingResult) {//TODO:обработать ошибки, добавить @Valid
        equipmentValidator.validateUpdate(id, equipmentDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationErrorException(bindingResult);
        }
        EquipmentDTO updated = equipmentService.update(id, equipmentDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable int id) {
        System.out.println("Trying to delete " + id);
        boolean deleted = equipmentService.deleteEquipmentById(id);
        if (!deleted)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
