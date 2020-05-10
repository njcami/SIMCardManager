package com.example.simsmanagerclient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.constraints.NotBlank;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AddressOperationException extends SimCardManagerException {
    public AddressOperationException(final @NotBlank String message) {
        super(message);
    }
}
