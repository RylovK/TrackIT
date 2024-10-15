package org.example.trackit.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class JobNotFoundException extends EntityNotFoundException {

    public JobNotFoundException(String message) {
        super(message);
    }
}
