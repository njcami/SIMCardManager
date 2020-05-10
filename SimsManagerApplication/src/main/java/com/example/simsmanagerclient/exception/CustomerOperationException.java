package com.example.simsmanagerclient.exception;

import javax.validation.constraints.NotBlank;

public class CustomerOperationException extends SimCardManagerException {
    public CustomerOperationException(final @NotBlank String message) {
        super(message);
    }
}
