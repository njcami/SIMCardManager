package com.example.simsmanagerclient.controllers;

import com.example.simsmanagerclient.dto.CustomerCreateDto;
import com.example.simsmanagerclient.dto.CustomerDto;
import com.example.simsmanagerclient.dto.CustomerUpdateDto;
import com.example.simsmanagerclient.exception.SimCardManagerException;
import com.example.simsmanagerclient.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@AllArgsConstructor
@RestController
public class CustomerController {

    private final CustomerService customerService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/customer", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public CustomerDto createCustomer(final @NonNull @RequestBody CustomerCreateDto customer) {
        return customerService.createCustomer(customer);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/customer/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public CustomerDto updateCustomer(final @Min(1L) @PathVariable Long customerId,
                                      final @NonNull @RequestBody CustomerUpdateDto customer)
                                        throws SimCardManagerException {
        return customerService.updateCustomer(customerId, customer);
    }
}
