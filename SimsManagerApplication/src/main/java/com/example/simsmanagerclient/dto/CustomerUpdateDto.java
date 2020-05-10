package com.example.simsmanagerclient.dto;

import com.example.simsmanagerclient.entities.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CustomerUpdateDto {

    @Min(1L)
    private Long id;

    private String firstName;
    private String lastName;
    private String idCardNumber;
    private String contactNumber;
    private String emailAddress;
    private LocalDate birthDate;
    private AddressUpdateDto addressDto;

    public static Customer updateFields(final @NonNull Customer customerToUpdate,
                                        final @NonNull CustomerUpdateDto customerDto) {
        customerToUpdate.setFirstName(customerDto.getFirstName());
        customerToUpdate.setLastName(customerDto.getLastName());
        customerToUpdate.setIdCardNumber(customerDto.getIdCardNumber());
        customerToUpdate.setEmailAddress(customerDto.getEmailAddress());
        customerToUpdate.setContactNumber(customerDto.getContactNumber());
        customerToUpdate.setBirthDate(customerDto.getBirthDate());
        customerToUpdate.setLastUpdateDate(LocalDateTime.now());

        return customerToUpdate;
    }
}
