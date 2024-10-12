package org.example.trackit.util;

import lombok.AllArgsConstructor;
import org.example.trackit.dto.PartNumberDTO;
import org.example.trackit.entity.properties.PartNumber;
import org.example.trackit.services.impl.PartNumberService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@AllArgsConstructor
public class PartNumberValidator implements Validator {

    private final PartNumberService partNumberService;

    @Override
    public boolean supports(Class<?> clazz) {
        return PartNumber.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PartNumberDTO partNumberDTO = (PartNumberDTO) target;
        Optional<PartNumber> founded = partNumberService.findPartNumberByNumber(partNumberDTO.getNumber());
        if (founded.isPresent()) {
            errors.rejectValue("partNumber", "duplicate", "Part number already exists");
        }
    }
}
