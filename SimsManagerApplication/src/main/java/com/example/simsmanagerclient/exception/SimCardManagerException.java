package com.example.simsmanagerclient.exception;

import javax.validation.constraints.NotBlank;

public class SimCardManagerException extends Exception {
    public SimCardManagerException(final @NotBlank String message) {
        super(message);
    }
}
