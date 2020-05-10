package com.example.simsmanagerclient.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.example.simsmanagerclient.entities.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class AddressDto extends AddressCreateDto {

    private final Long id;

    private final LocalDateTime createdDate;
    private final LocalDateTime lastUpdatedDate;

    public AddressDto(final Long id,
                      final Integer houseNumber,
                      final String houseName,
                      final String streetName,
                      final String locality,
                      final String town,
                      final String country,
                      final String postCode,
                      final LocalDateTime createdDate,
                      final LocalDateTime lastUpdatedDate) {
        super(houseNumber, houseName, streetName, locality, town, country, postCode);
        this.id = id;
        this.createdDate = createdDate;
        this.lastUpdatedDate = lastUpdatedDate;
    }

    /**
     * Excluding created and last updated dates from JSON serialization.
     * @param address as loaded from database.
     * @return required fields for JSON output.
     */
    public static AddressDto transform(final Address address) {
        return new AddressDto(address.getId(),
                                address.getHouseNumber(),
                                address.getHouseName(),
                                address.getStreetName(),
                                address.getLocality(),
                                address.getTown(),
                                address.getCountry(),
                                address.getPostcode(),
                                address.getCreatedDate(),
                                address.getLastUpdatedDate());
    }
}
