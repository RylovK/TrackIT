package org.example.trackit.exceptions;

import jakarta.persistence.EntityExistsException;

public class JobAlreadyExistException extends EntityExistsException {
    public JobAlreadyExistException(String message) {
        super(message);
    }
}
