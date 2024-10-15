package org.example.trackit.exceptions;

import jakarta.persistence.EntityExistsException;

public class PartNumberAlreadyExistException extends EntityExistsException {
    public PartNumberAlreadyExistException(String s) {
        super(s);
    }
}
