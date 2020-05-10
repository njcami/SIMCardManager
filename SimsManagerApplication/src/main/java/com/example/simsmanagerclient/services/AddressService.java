package com.haud.simsmanagerclient.services;

import com.haud.simsmanagerclient.dto.AddressCreateDto;
import com.haud.simsmanagerclient.dto.AddressUpdateDto;
import com.haud.simsmanagerclient.entities.Address;
import com.haud.simsmanagerclient.exception.AddressOperationException;
import com.haud.simsmanagerclient.repositories.AddressRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.haud.simsmanagerclient.dto.AddressCreateDto.transform;
import static com.haud.simsmanagerclient.dto.AddressUpdateDto.updateFields;
import static java.lang.String.format;

@Slf4j
@AllArgsConstructor
@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public Address createAddress(final @NonNull AddressCreateDto addressDto) {
        log.debug("Saving address :{}", addressDto);
        return addressRepository.save(transform(addressDto));
    }

    public Address updateAddress(final @NonNull AddressUpdateDto addressDto) throws AddressOperationException {
        log.debug("Updating address :{}", addressDto);
        final Address address = addressRepository.findById(addressDto.getId()).orElseThrow(() ->
                new AddressOperationException(format("Address with id:[%d] was not found.", addressDto.getId())));
        return addressRepository.save(updateFields(address, addressDto));
    }

}
