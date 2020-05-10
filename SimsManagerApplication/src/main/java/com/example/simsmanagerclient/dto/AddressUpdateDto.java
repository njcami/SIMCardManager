package com.example.simsmanagerclient.dto;

import com.example.simsmanagerclient.entities.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AddressUpdateDto {

    @Min(1L)
    private Long id;

    @Min(1)
    private Integer houseNumber;

    private String houseName;
    private String streetName;
    private String locality;
    private String town;
    private String country;
    private String postcode;

    public static Address updateFields(final @NonNull Address addressToUpdate,
                                       final @NonNull AddressUpdateDto addressDto) {
        addressToUpdate.setHouseNumber(addressDto.getHouseNumber());
        addressToUpdate.setHouseName(addressDto.getHouseName());
        addressToUpdate.setStreetName(addressDto.getStreetName());
        addressToUpdate.setLocality(addressDto.getLocality());
        addressToUpdate.setTown(addressDto.getTown());
        addressToUpdate.setPostcode(addressDto.getPostcode());
        addressToUpdate.setCountry(addressDto.getCountry());
        addressToUpdate.setLastUpdatedDate(LocalDateTime.now());

        return addressToUpdate;
    }
}
