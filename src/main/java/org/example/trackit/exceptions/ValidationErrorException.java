package org.example.trackit.exceptions;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class ValidationErrorException extends RuntimeException {

    private final BindingResult bindingResult;

    public ValidationErrorException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }
}
