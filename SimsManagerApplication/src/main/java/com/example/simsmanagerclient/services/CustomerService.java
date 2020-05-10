package com.example.simsmanagerclient.services;

import com.example.simsmanagerclient.config.Config;
import com.example.simsmanagerclient.dto.CustomerCreateDto;
import com.example.simsmanagerclient.dto.CustomerDto;
import com.example.simsmanagerclient.dto.CustomerUpdateDto;
import com.example.simsmanagerclient.entities.Customer;
import com.example.simsmanagerclient.exception.CustomerBadRequestException;
import com.example.simsmanagerclient.exception.CustomerOperationException;
import com.example.simsmanagerclient.exception.SimCardManagerException;
import com.example.simsmanagerclient.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Min;
import java.util.List;

import static com.example.simsmanagerclient.dto.CustomerDto.transform;
import static com.example.simsmanagerclient.dto.CustomerUpdateDto.updateFields;
import static com.haud.simsmanagerclient.helper.Utils.isTodayDaysBeforeCustomerBirthday;
import static com.haud.simsmanagerclient.helper.Utils.writeToExportFile;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@AllArgsConstructor
@ConfigurationProperties
public class CustomerService {

    private final Config config;
    private final AddressService addressService;
    private final CustomerRepository customerRepository;
    private final EmailService emailService;

    /**
     * At every preset time of the day, send an offer email to selected customers by remaining days to
     * their birthday.
     */
    @Scheduled(cron = "${scheduling.before.birthdate.cron}")
    public void sendEmailNotification() {
        if (config.isSchedulingActive()) {
            log.info("Scheduling is active on this instance, processing pre-birthday discount notification.");
            getCustomersWithDaysToBirthDay(config.getDaysBeforeBirthDate(), false)
                .forEach(customer -> {
                    final String personalisedMessage = config.getEmailMessage()
                            .replace(config.getFirstNamePlaceholder(), customer.getFirstName())
                            .replace(config.getDiscountPlaceholder(), config.getDiscountPercentage());
                    log.info(format("Sending birthday email for customer id:%d:[%s]", customer.getId(), personalisedMessage));
                    final String emailSubject = config.getEmailSubject()
                            .replace(config.getFirstNamePlaceholder(), customer.getFirstName());
                    emailService.sendEmailMessage(customer.getEmailAddress(), emailSubject, personalisedMessage);
                });
        } else {
            log.info("Scheduling is not active on this instance.");
        }
    }

    /**
     * Export customers as well as their SIM cards who happen to have their birthday on the day.
     * @return list of customers who have their birthday today as well as their SIM Cards.
     */
    @Scheduled(cron = "${scheduling.at.birthdate.cron}")
    public List<CustomerDto> exportBirthdayCustomersWithSimCardsToFile() {
        if (config.isSchedulingActive()) {
            log.info("Scheduling is active on this instance, processing birthday customers with sim cards export.");
            return writeToExportFile(getCustomersWithDaysToBirthDay(0, true),
                    config.getBirthdayExportFilename());
        } else {
            log.info("Scheduling is not active on this instance.");
        }
        return null;
    }

    public List<CustomerDto> getCustomersWithDaysToBirthDay(final @Min(0) Integer daysToBirthDay,
                                                            final boolean includeCustomerSimCards) {
        final List<Customer> customerList = customerRepository.findAll();
        return customerList.stream()
                .filter(customer -> isTodayDaysBeforeCustomerBirthday(daysToBirthDay, customer))
                .map(customer -> CustomerDto.transform(customer, includeCustomerSimCards))
                .collect(toList());
    }

    public CustomerDto createCustomer(final @NonNull CustomerCreateDto customerDto) {
        return transform(customerRepository.save(CustomerCreateDto.transform(customerDto)));
    }

    public CustomerDto updateCustomer(final @Min(1L) Long customerId,
                                        final @NonNull CustomerUpdateDto customerDto)
                                        throws SimCardManagerException {
        if (!customerId.equals(customerDto.getId())) {
            log.error("Mismatch in customer ids found:[{}] [{}]", customerId, customerDto.getId());
            throw new CustomerBadRequestException("Mismatch in customer ids found.");
        }
        if (customerDto.getAddressDto() != null) {
            addressService.updateAddress(customerDto.getAddressDto());
        }
        final Customer customer = customerRepository.findById(customerDto.getId()).orElseThrow(() ->
            new CustomerOperationException(format("Customer with id:[%d] was not found.", customerDto.getId())));
        return transform(customerRepository.save(updateFields(customer, customerDto)));
    }

    Customer findCustomerById(final @Min(1L) Long customerId) throws CustomerOperationException {
        return customerRepository.findById(customerId).orElseThrow(() ->
                new CustomerOperationException(format("Customer with id:[%d] was not found.", customerId)));
    }

}
