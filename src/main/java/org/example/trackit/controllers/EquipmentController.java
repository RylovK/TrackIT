package org.example.trackit.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.properties.AllocationStatus;
import org.example.trackit.entity.properties.HealthStatus;
import org.example.trackit.services.EquipmentService;
import org.example.trackit.util.EquipmentValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/")
@AllArgsConstructor
@Tag(name = "Equipment API", description = "Operations related to equipment management")
public class EquipmentController {

    //////////////////////////////////TODO: validation

    private final EquipmentService<EquipmentDTO> equipmentService;
    private final EquipmentValidator equipmentValidator;

    @GetMapping
    @Operation(summary = "Find all equipment", description = "Get a list of all equipment with filtration and pagination")//TODO: нужна страница index, где будут зашружены все партномера, работы и статусы
    public ResponseEntity<Page<EquipmentDTO>> getAllEquipment(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "25") int size,
                                                              @RequestParam(required = false) Map<String, String> filters) {
        Page<EquipmentDTO> dtoPage = equipmentService.findAllEquipment(filters, PageRequest.of(page, size));
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentDTO> getEquipment(@PathVariable int id) {
        EquipmentDTO equipmentDTO = equipmentService.findEquipmentById(id);
        if (equipmentDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);//TODO:создать настраиваемый ответ с сообщением об ошибке
        }
        return new ResponseEntity<>(equipmentDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EquipmentDTO> createEquipment(@RequestBody EquipmentDTO equipmentDTO, BindingResult bindingResult) {
        equipmentValidator.validate(equipmentDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);//TODO:создать настраиваемый ответ с сообщением об ошибке
        }
        EquipmentDTO createdEquipment = equipmentService.save(equipmentDTO);
        return new ResponseEntity<>(createdEquipment, HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<EquipmentDTO> updateEquipment(@RequestBody EquipmentDTO equipmentDTO, BindingResult bindingResult) {//TODO:обработать ошибки, добавить @Valid
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);//TODO:создать настраиваемый ответ с сообщением об ошибке
        }
        EquipmentDTO updated = equipmentService.save(equipmentDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
    @DeleteMapping
    public ResponseEntity<Void> deleteEquipment(@PathVariable int id) {
        boolean deleted = equipmentService.deleteEquipmentById(id);
        if (!deleted)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
