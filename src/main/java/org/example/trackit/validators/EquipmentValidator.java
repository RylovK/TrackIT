package org.example.trackit.validators;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trackit.dto.CreateEquipmentDTO;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.Equipment;
import org.example.trackit.repository.EquipmentRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class EquipmentValidator implements Validator {

    private final EquipmentRepository equipmentRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return EquipmentDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        log.info("Validating equipment {}", target);
        CreateEquipmentDTO equipment = (CreateEquipmentDTO) target;
        log.info("Validating equipment {}", equipment.getSerialNumber());
        Optional<Equipment> founded =
                equipmentRepository.findByPartNumberAndSerialNumber(equipment.getPartNumber(),
                        equipment.getSerialNumber());
        if (founded.isPresent()) {
            errors.rejectValue("Equipment", "duplicate", "Equipment already exists");
        }
    }
}
