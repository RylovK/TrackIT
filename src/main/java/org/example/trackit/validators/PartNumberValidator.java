package org.example.trackit.validators;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trackit.dto.PartNumberDTO;
import org.example.trackit.entity.properties.PartNumber;
import org.example.trackit.services.PartNumberService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class PartNumberValidator implements Validator {

    private final PartNumberService partNumberService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return PartNumber.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        PartNumberDTO partNumberDTO = (PartNumberDTO) target;
        Optional<PartNumber> founded = partNumberService.findPartNumberByNumber(partNumberDTO.getNumber());
        if (founded.isPresent()) {
            errors.rejectValue("number", "duplicate", "Part number already exists");
        }
    }
}
