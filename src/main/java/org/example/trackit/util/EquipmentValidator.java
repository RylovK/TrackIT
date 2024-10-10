package org.example.trackit.util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.Equipment;
import org.example.trackit.services.EquipmentService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class EquipmentValidator implements Validator {

    private final EquipmentService equipmentService;

    @Override
    public boolean supports(Class<?> clazz) {
        return EquipmentDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.info("Validating equipment {}", target);
        EquipmentDTO equipment = (EquipmentDTO) target;
        log.info("Validating equipment {}", equipment.getSerialNumber());
        Optional<Equipment> founded =
                equipmentService.findByPartNumberAndSerialNumber(equipment.getPartNumber(),
                        equipment.getSerialNumber());
        if (founded.isPresent()) {
            errors.rejectValue("Equipment", "duplicate", "Equipment already exists");
        }
    }
}
