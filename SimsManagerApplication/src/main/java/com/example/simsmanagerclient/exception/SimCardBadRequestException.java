package com.example.simsmanagerclient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.constraints.NotBlank;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SimCardBadRequestException extends SimCardManagerException {
    public SimCardBadRequestException(final @NotBlank String message) {
        super(message);
    }
}
