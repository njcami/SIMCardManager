package com.example.simsmanagerclient.dto;

import com.example.simsmanagerclient.entities.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCreateDto {

    @NotBlank(message = "First name should not be empty.")
    private String firstName;

    @NotBlank(message = "Last name should not be empty.")
    private String lastName;

    @Length(min = 3, max = 10, message = "IdCard should be between 3 and 10 characters long.")
    private String idCardNumber;

    private String contactNumber;

    private String emailAddress;

    @NotNull
    private LocalDate birthDate;

    @NotNull(message = "Address should not be null.")
    private AddressCreateDto addressDto;

    public static Customer transform(final CustomerCreateDto customerDto) {
        if (customerDto == null) {
            return null;
        }
        return Customer.builder()
                .firstName(customerDto.getFirstName())
                .lastName(customerDto.getLastName())
                .emailAddress(customerDto.getEmailAddress())
                .idCardNumber(customerDto.getIdCardNumber())
                .contactNumber(customerDto.getContactNumber())
                .birthDate(customerDto.getBirthDate())
                .address(AddressCreateDto.transform(customerDto.getAddressDto()))
                .createdDate(LocalDateTime.now())
                .build();
    }
}
