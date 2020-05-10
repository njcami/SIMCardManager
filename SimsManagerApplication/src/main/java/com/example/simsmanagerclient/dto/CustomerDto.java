package com.example.simsmanagerclient.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.example.simsmanagerclient.entities.Customer;
import com.example.simsmanagerclient.entities.SimCard;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Getter
@Setter
@JsonInclude(NON_NULL)
public class CustomerDto extends CustomerCreateDto {
    private final Long id;

    private final LocalDateTime createdDate;
    private final LocalDateTime lastUpdatedDate;

    private final List<SimCardDto> simCardDtoList;

    public CustomerDto(final Long id,
                       final String firstName,
                       final String lastName,
                       final String idCardNumber,
                       final String contactNumber,
                       final String emailAddress,
                       final LocalDate birthDate,
                       final AddressDto address,
                       final LocalDateTime createdDate,
                       final LocalDateTime lastUpdatedDate,
                       final List<SimCardDto> simCardDtoList) {
        super(firstName, lastName, idCardNumber, contactNumber, emailAddress, birthDate, address);
        this.id = id;
        this.createdDate = createdDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.simCardDtoList = simCardDtoList;
    }

    /**
     * Excluding last updated date from JSON serialization as an example to show that we can select
     * which fields we want to show from the endpoints.
     * @param customer customer as retrieved from database.
     * @return customer without the last updated date.
     */
    public static CustomerDto transform(final @NonNull Customer customer) {
        return transform(customer, false);
    }

    public static CustomerDto transform(final @NonNull Customer customer,
                                         final boolean includeSimCards) {
        List<SimCardDto> simCards = null;
        if (includeSimCards && !isEmpty(customer.getSimCards())) {
            simCards = customer.getSimCards().stream()
                                            .map(SimCardDto::transform)
                                            .collect(toList());
        }
        return new CustomerDto(customer.getId(),
                                customer.getFirstName(),
                                customer.getLastName(),
                                customer.getIdCardNumber(),
                                customer.getContactNumber(),
                                customer.getEmailAddress(),
                                customer.getBirthDate(),
                                AddressDto.transform(customer.getAddress()),
                                customer.getCreatedDate(),
                                customer.getLastUpdateDate(),
                                simCards);
    }
}
