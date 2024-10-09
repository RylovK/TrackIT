package org.example.trackit.controllers;

import lombok.AllArgsConstructor;
import org.example.trackit.dto.BasicEquipmentDTO;
import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.parts.Equipment;
import org.example.trackit.services.EquipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipment")
@AllArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping
    public List<EquipmentDTO> getAllEquipment() {
        return equipmentService.findAllEquipment();
    }

    @PostMapping("/find") //return BasicEquipmentDTO or CertifiedEquipmentDTO
    public EquipmentDTO getEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        return equipmentService.findEquipment(equipmentDTO);
    }
//////////////////////////////////
    @PostMapping
    public ResponseEntity<Equipment> createEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        System.out.println("Получен объект: " + equipmentDTO);
        equipmentService.createEquipment(equipmentDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
