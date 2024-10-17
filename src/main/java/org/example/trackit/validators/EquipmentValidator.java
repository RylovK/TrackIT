package org.example.trackit.validators;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.dto.CreateEquipmentDTO;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.CertifiedEquipment;
import org.example.trackit.entity.Equipment;
import org.example.trackit.entity.properties.AllocationStatus;
import org.example.trackit.entity.properties.CertificationStatus;
import org.example.trackit.entity.properties.HealthStatus;
import org.example.trackit.entity.properties.PartNumber;
import org.example.trackit.repository.CertifiedEquipmentRepository;
import org.example.trackit.repository.EquipmentRepository;
import org.example.trackit.repository.PartNumberRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class EquipmentValidator implements Validator {

    private final EquipmentRepository equipmentRepository;
    private final PartNumberRepository partNumberRepository;
    private final CertifiedEquipmentRepository certifiedEquipmentRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return EquipmentDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        CreateEquipmentDTO equipment = (CreateEquipmentDTO) target;
        if (isPartNumberValid(equipment.getPartNumber(), errors)) {
            equipmentRepository.findByPartNumberAndSerialNumber(equipment.getPartNumber(),
                    equipment.getSerialNumber()).ifPresentOrElse(
                    _ -> errors.reject("Equipment", "Equipment already exists"),
                    () -> {
                    });
        }
    }

    private boolean isPartNumberValid(String partNumber, Errors errors) {
        Optional<PartNumber> founded = partNumberRepository.findByNumber(partNumber);
        if (founded.isEmpty()) {
            errors.rejectValue("PartNumber", "invalid", "Part number does not exist");
            return false;
        }
        return true;
    }

    // Метод для валидации обновления оборудования
    public void validateUpdate(int id, EquipmentDTO dto, Errors errors) {
        System.out.println("Validate update for id:" + id + " dto:" + dto.getPartNumber() + ":" + dto.getSerialNumber());
        Equipment existing = equipmentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (isPartNumberValid(dto.getPartNumber(), errors)) {
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
            } else if (existing.getAllocationStatus() == AllocationStatus.ON_BASE
                    && dto.getAllocationStatus() == AllocationStatus.ON_LOCATION
                    && dto.getHealthStatus() != HealthStatus.RITE) {
                errors.reject("equipmentCondition", "You can send to job only RITE equipment");
            }
        }
    }

    public void validateCertification(int id, CertifiedEquipmentDTO dto, Errors errors) {
        CertifiedEquipment existing = certifiedEquipmentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (existing.getAllocationStatus() == AllocationStatus.ON_BASE
                && dto.getAllocationStatus() == AllocationStatus.ON_LOCATION
                && existing.getCertificationStatus() == CertificationStatus.EXPIRED) {
            errors.rejectValue("certificationStatus", "expired", "You can't send to job expired equipment");
        }
    }
}

