package com.example.simsmanagerclient.services;

import com.example.simsmanagerclient.config.Config;
import com.example.simsmanagerclient.dto.CustomerCreateDto;
import com.example.simsmanagerclient.dto.CustomerDto;
import com.example.simsmanagerclient.dto.SimCardCreateDto;
import com.example.simsmanagerclient.entities.Customer;
import com.example.simsmanagerclient.entities.SimCard;
import com.example.simsmanagerclient.helper.Utils;
import com.example.simsmanagerclient.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.simsmanagerclient.dto.CustomerCreateDto.transform;
import static com.example.simsmanagerclient.helper.Utils.createRandomCustomer;
import static com.haud.simsmanagerclient.helper.Utils.createRandomSimCard;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock private Config config;
    @Mock private AddressService addressService;
    @Mock private EmailService emailService;
    @Mock private CustomerRepository customerRepository;

    @InjectMocks private CustomerService customerService;

    @Test
    public void test_whenCreateCustomer_returnsACustomerDto() {
        final CustomerCreateDto customerCreateDto = createRandomCustomer(0);
        final Customer customer = transform(customerCreateDto);
        customer.setId(1L); // otherwise the transform will give us an object without the id.
        lenient().when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        assertEquals(CustomerDto.transform(customer), customerService.createCustomer(customerCreateDto));
    }

    @Test
    public void test_whenCreateCustomer_customerIsNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> customerService.createCustomer(null));
    }

    @Test
    public void test_whenCreateCustomer_customerIsEmpty_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> customerService.createCustomer(new CustomerCreateDto()) );
    }

    @Test
    public void test_thatWhenSendingBirthdayOffer_customers_areCorrectlyChosen() {
        final Integer correctDaysToBirthday = 7;
        when(customerRepository.findAll())
                .thenReturn(singletonList(transform(createRandomCustomer(correctDaysToBirthday))));

        assertEquals(1,
                customerService.getCustomersWithDaysToBirthDay(correctDaysToBirthday, false).size());

        assertEquals(0,
                customerService.getCustomersWithDaysToBirthDay(0, false).size());

        assertEquals(0,
                customerService.getCustomersWithDaysToBirthDay(correctDaysToBirthday - 1, false).size());

        assertEquals(0,
                customerService.getCustomersWithDaysToBirthDay(correctDaysToBirthday + 1, false).size());
    }

    @Test
    public void test_emailsToCustomersAreSent_onlyIfSchedulingIsActive() {
        final String subject = "SUBJECT";
        final String namePlaceholder = "{name}";
        final String discountPlaceholder = "{discount}";
        final String discount = "30%";
        final String message = "message";

        final Customer birthdayTodayCustomer = transform(createRandomCustomer(0));
        final Customer birthdayTomorrowCustomer = transform(createRandomCustomer(1));

        //when scheduling is inactive, no emails are sent
        when(config.isSchedulingActive()).thenReturn(false);
        when(config.getEmailSubject()).thenReturn(subject);
        when(config.getEmailMessage()).thenReturn(message + namePlaceholder + discountPlaceholder);
        when(config.getFirstNamePlaceholder()).thenReturn(namePlaceholder);
        when(config.getDiscountPlaceholder()).thenReturn(discountPlaceholder);
        when(config.getDiscountPercentage()).thenReturn(discount);

        when(customerRepository.findAll())
                .thenReturn(Arrays.asList(birthdayTodayCustomer, birthdayTomorrowCustomer));

        customerService.sendEmailNotification();
        verify(emailService, never()).sendEmailMessage(anyString(), anyString(), anyString());

        // switching on scheduling
        when(config.isSchedulingActive()).thenReturn(true);
        customerService.sendEmailNotification();
        verify(emailService, times(1)).sendEmailMessage(birthdayTodayCustomer.getEmailAddress(), subject,
                message + birthdayTodayCustomer.getFirstName() + discount);
    }

    @Test
    public void test_thatWhenExportingCustomersAndSims_onlyThoseWithCorrectBirthdayAreChosen() {
        final SimCardCreateDto simCard = createRandomSimCard(true, 0);
        final Customer birthdayTodayCustomer = transform(simCard.getCustomerDto());
        final String todaysIdCardNumber = birthdayTodayCustomer.getIdCardNumber();

        final Customer birthdayTomorrowCustomer = transform(createRandomCustomer(1));

        //when scheduling is inactive, no customers are exported
        when(config.isSchedulingActive()).thenReturn(false);
        when(config.getBirthdayExportFilename()).thenReturn("test.json");
        when(customerRepository.findAll())
                .thenReturn(Arrays.asList(birthdayTodayCustomer, birthdayTomorrowCustomer));
        final List<CustomerDto> exportedCustomers = customerService.exportBirthdayCustomersWithSimCardsToFile();
        assertNull(customerService.exportBirthdayCustomersWithSimCardsToFile());

        // switching on scheduling
        when(config.isSchedulingActive()).thenReturn(true);
        final List<CustomerDto> exportedCustomers2 = customerService.exportBirthdayCustomersWithSimCardsToFile();
        assertEquals(1, exportedCustomers2.size());
        assertEquals(todaysIdCardNumber, exportedCustomers2.get(0).getIdCardNumber());
    }
}
