package com.example.simsmanagerclient.dto;

import com.example.simsmanagerclient.entities.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressCreateDto {

    @Min(value = 1, message = "House number should be greater than zero.")
    private Integer houseNumber;

    private String houseName;

    @NotBlank(message = "Please enter street name.")
    private String streetName;

    private String locality;

    @NotBlank(message ="Please enter town.")
    private String town;

    @NotBlank(message = "Please enter the country.")
    private String country;

    private String postcode;

    public static Address transform(final AddressCreateDto addressDto) {
        return addressDto == null ? null : Address.builder()
                                                .houseNumber(addressDto.getHouseNumber())
                                                .houseName(addressDto.getHouseName())
                                                .streetName(addressDto.getStreetName())
                                                .locality(addressDto.getLocality())
                                                .town(addressDto.getTown())
                                                .postcode(addressDto.getPostcode())
                                                .country(addressDto.getCountry())
                                                .createdDate(LocalDateTime.now())
                                                .build();
    }

    public static AddressCreateDto transform(final Address address) {
        return new AddressCreateDto(address.getHouseNumber(),
                                    address.getHouseName(),
                                    address.getStreetName(),
                                    address.getLocality(),
                                    address.getTown(),
                                    address.getCountry(),
                                    address.getPostcode());
    }
}
