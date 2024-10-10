package org.example.trackit.util.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class PartNumberNotFoundException extends EntityNotFoundException {
    public PartNumberNotFoundException(String message) {
    }
}
