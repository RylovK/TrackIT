package org.example.trackit.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class PartNumberNotFoundException extends EntityNotFoundException {
    public PartNumberNotFoundException(String message) {
        super(message);
    }
}
