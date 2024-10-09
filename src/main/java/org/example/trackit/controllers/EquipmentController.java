package org.example.trackit.controllers;

import lombok.AllArgsConstructor;
import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.Equipment;
import org.example.trackit.services.EquipmentService;
import org.example.trackit.util.EquipmentValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipment")
@AllArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;
    private final EquipmentValidator equipmentValidator;

    @GetMapping
    public ResponseEntity<List<EquipmentDTO>> getAllEquipment() {
        List<EquipmentDTO> dtoList = equipmentService.findAllEquipment();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentDTO> getEquipment(@PathVariable int id) {
        EquipmentDTO equipmentDTO = equipmentService.findEquipmentById(id);
        if (equipmentDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);//TODO:создать настраиваемый ответ с сообщением об ошибке
        }
        return new ResponseEntity<>(equipmentDTO, HttpStatus.OK);
    }
//////////////////////////////////TODO: validation
    @PostMapping
    public ResponseEntity<EquipmentDTO> createEquipment(@RequestBody EquipmentDTO equipmentDTO, BindingResult bindingResult) {
        equipmentValidator.validate(equipmentDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);//TODO:создать настраиваемый ответ с сообщением об ошибке
        }
        EquipmentDTO createdEquipment = equipmentService.createEquipment(equipmentDTO);
        return new ResponseEntity<>(createdEquipment, HttpStatus.CREATED);
    }

    @GetMapping("/certified")
    public ResponseEntity<List<CertifiedEquipmentDTO>> getAllCertifiedEquipment() {
        List<CertifiedEquipmentDTO> allCertifiedEquipment= equipmentService.findAllCertifiedEquipment();
        return new ResponseEntity<>(allCertifiedEquipment, HttpStatus.OK);
    }

    @PostMapping("/certified")
    public ResponseEntity<CertifiedEquipmentDTO> createCertifiedEquipment(@RequestBody CertifiedEquipmentDTO equipmentDTO, BindingResult bindingResult) {
        equipmentValidator.validate(equipmentDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);//TODO:создать настраиваемый ответ с сообщением об ошибке
        }
        CertifiedEquipmentDTO createdEquipment = (CertifiedEquipmentDTO) equipmentService.createEquipment(equipmentDTO);
        return new ResponseEntity<>(createdEquipment, HttpStatus.CREATED);
    }
}
