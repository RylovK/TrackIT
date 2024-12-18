package org.example.trackit.exceptions;

import jakarta.persistence.EntityExistsException;
import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class PartNumberAlreadyExistException extends EntityExistsException {

    private final BindingResult bindingResult;

    public PartNumberAlreadyExistException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }
}
