package org.example.trackit.controllers;

import lombok.AllArgsConstructor;
import org.example.trackit.dto.PartNumberDTO;
import org.example.trackit.services.impl.PartNumberService;
import org.example.trackit.util.PartNumberValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/partNumber")
@AllArgsConstructor
public class PartNumberController {

    private final PartNumberService partNumberService;
    private final PartNumberValidator partNumberValidator;

    @GetMapping
    public ResponseEntity<List<PartNumberDTO>> getAllPartNumbers() {
        List<PartNumberDTO> founded = partNumberService.findAllPartNumbers();
        return ResponseEntity.ok(founded);
    }

    @PostMapping
    public ResponseEntity<PartNumberDTO> createPartNumber(@RequestBody PartNumberDTO partNumberDTO, BindingResult bindingResult) {
        partNumberValidator.validate(partNumberDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);//TODO:создать настраиваемый ответ с сообщением об ошибке
        }
        PartNumberDTO createdPN = partNumberService.createPartNumber(partNumberDTO);
        return new ResponseEntity<>(createdPN, HttpStatus.CREATED);
    }

    //getAllEquipmentByPartNumber

}
