package org.example.trackit.validators;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trackit.dto.CreateEquipmentDTO;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.Equipment;
import org.example.trackit.entity.properties.AllocationStatus;
import org.example.trackit.entity.properties.HealthStatus;
import org.example.trackit.repository.EquipmentRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
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


    // Метод для валидации обновления оборудования. TODO: убрать валидацию из сервиса и решить проблему транзакции при обновлении
    public void validateUpdate(int id, EquipmentDTO dto, Errors errors) {
        Equipment existing = equipmentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        // Проверка, что можно изменять только location, если статус "On location"
        if (existing.getAllocationStatus() == AllocationStatus.ON_LOCATION
                && dto.getAllocationStatus() != AllocationStatus.ON_BASE) {
            errors.reject("statusChange", "Cannot change state while equipment ON_LOCATION");
        }

        // Проверка, что Job обязателен при смене статуса на "On location"
        if (dto.getAllocationStatus() == AllocationStatus.ON_LOCATION && dto.getJobName() == null) {
            errors.reject("jobRequired", "Job must be assigned when setting status to ON_LOCATION");
        }

        // Обработка смены статуса с "On location" на "On base" и обратно
        if (existing.getAllocationStatus() == AllocationStatus.ON_LOCATION
                && dto.getAllocationStatus() == AllocationStatus.ON_BASE) {
            dto.setHealthStatus(HealthStatus.RONG);
            dto.setJobName(null);
            dto.setAllocationStatusLastModified(LocalDateTime.now());
        } else if (existing.getAllocationStatus() == AllocationStatus.ON_BASE
                && dto.getAllocationStatus() == AllocationStatus.ON_LOCATION
                && dto.getHealthStatus() != HealthStatus.RITE) {
            errors.reject("equipmentCondition", "You can send to job only RITE equipment");
        }
    }
}

