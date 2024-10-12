package org.example.trackit.controllers;

import lombok.AllArgsConstructor;
import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.entity.properties.AllocationStatus;
import org.example.trackit.entity.properties.CertificationStatus;
import org.example.trackit.entity.properties.HealthStatus;
import org.example.trackit.services.CertifiedEquipmentService;
import org.example.trackit.util.EquipmentValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/certified")
@AllArgsConstructor
public class CertifiedEquipmentController {

    private final CertifiedEquipmentService certifiedEquipmentService;
    private final EquipmentValidator equipmentValidator;


    @GetMapping
    public ResponseEntity<Page<CertifiedEquipmentDTO>> getAllCertifiedEquipment(@RequestParam(defaultValue = "0") int page,
                                                                                @RequestParam(defaultValue = "25") int size,
                                                                                @RequestParam(required = false) String partNumber,
                                                                                @RequestParam(required = false) String serialNumber,
                                                                                @RequestParam(required = false) HealthStatus healthStatus,
                                                                                @RequestParam(required = false) AllocationStatus allocationStatus,
                                                                                @RequestParam(required = false) String jobName,
                                                                                @RequestParam(required = false) CertificationStatus certificationStatus,
                                                                                @RequestParam(defaultValue = "nextCertificationDate") String sortBy,
                                                                                @RequestParam(defaultValue = "asc") String sortDirection) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
        Page<CertifiedEquipmentDTO> allCertifiedEquipment = certifiedEquipmentService.
                findAllCertifiedEquipment(partNumber, serialNumber, healthStatus, allocationStatus, jobName, certificationStatus, pageRequest);
        return new ResponseEntity<>(allCertifiedEquipment, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertifiedEquipmentDTO> getCertifiedEquipmentById(@PathVariable ("id") Integer id) {
        CertifiedEquipmentDTO founded = certifiedEquipmentService.findCertifiedEquipmentById(id);
        if (founded == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(founded, HttpStatus.OK);
    }

    /**
     * This method takes DTO from creation form
     * @param equipmentDTO this param has two option: serial and part numbers only, and additional parameters for
     *                     certified equipment: certification date, certification period(Month) and file with certificate.
     * @param bindingResult Errors from form validation.
     * @return DTO of created equipment or validation errors
     */
    @PostMapping
    public ResponseEntity<CertifiedEquipmentDTO> createCertifiedEquipment(@RequestBody CertifiedEquipmentDTO equipmentDTO, BindingResult bindingResult) {
        equipmentValidator.validate(equipmentDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);//TODO:создать настраиваемый ответ с сообщением об ошибке
        }
        CertifiedEquipmentDTO createdEquipment = certifiedEquipmentService.save(equipmentDTO);
        return new ResponseEntity<>(createdEquipment, HttpStatus.CREATED);
    }
}
