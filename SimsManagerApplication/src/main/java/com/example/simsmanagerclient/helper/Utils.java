package com.example.simsmanagerclient.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.example.simsmanagerclient.dto.AddressCreateDto;
import com.example.simsmanagerclient.dto.CustomerCreateDto;
import com.example.simsmanagerclient.dto.CustomerDto;
import com.example.simsmanagerclient.dto.SimCardCreateDto;
import com.example.simsmanagerclient.entities.Customer;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang.RandomStringUtils.randomNumeric;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
public class Utils {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("CET");

    private Utils() {
    }

    private static final Random random = new Random();

    public static AddressCreateDto createRandomAddress() {
        return new AddressCreateDto(randomNumber(1, 500),
                randomAlphabetic(13),
                randomAlphabetic(50),
                randomAlphabetic(20),
                randomAlphabetic(15),
                randomAlphabetic(8),
                randomAlphanumeric(10));
    }

    public static CustomerCreateDto createRandomCustomer(final @Min(0) Integer birthdayInDays) {
        return new CustomerCreateDto(randomAlphabetic(15),
                randomAlphabetic(20),
                randomAlphanumeric(8),
                "+" + randomNumber(1, 999) + randomNumber(10000000, 99999999),
                randomAlphabetic(20) + "@" + randomAlphabetic(30) + "." + randomAlphabetic(4),
                LocalDate.now().plusDays(birthdayInDays),
                createRandomAddress());
    }

    public static SimCardCreateDto createRandomSimCard(final boolean withCustomer,
                                                       final Integer customerBirthdayInDays) {
        final SimCardCreateDto simCard = new SimCardCreateDto(randomNumeric(20),
                randomNumeric(15),
                null,
                randomNumber(100, 999),
                randomNumber(10000000, 99999999),
                null,
                randomAlphabetic(20),
                null,
                null);


        if (withCustomer) {
            final CustomerDto customerDto = CustomerDto.transform(CustomerCreateDto.transform(createRandomCustomer(customerBirthdayInDays)));
            simCard.setCustomerDto(customerDto);
            simCard.setMSISDN(randomNumeric(15));
            simCard.setFirstDeviceIMEI(randomNumeric(15));
            simCard.setFirstDeviceType(randomAlphanumeric(30));
        }
        return simCard;
    }

    private static int randomNumber(final int lowerBound, int upperBound) {
        return random.nextInt(upperBound - lowerBound) + lowerBound;
    }

    /**
     * Checks if in a preset amount of days in the future, it will be the customer's birthday.
     *
     * @param customer customer whose birth date is being checked.
     * @return true if the customer will have the birthday in the preset amount of days.
     */
    public static boolean isTodayDaysBeforeCustomerBirthday(final @Min(0) Integer daysBeforeBirthday,
                                                            final @NonNull Customer customer) {
        LocalDate daysFromToday = LocalDate.now().plusDays(daysBeforeBirthday);
        Integer dayOfMonth = daysFromToday.getDayOfMonth();
        Integer monthOfYear = daysFromToday.getMonthValue();

        LocalDate customerBirthDate = customer.getBirthDate();
        Integer birthDateDay = customerBirthDate.getDayOfMonth();
        Integer birthDateMonth = customerBirthDate.getMonthValue();

        return dayOfMonth.equals(birthDateDay) && monthOfYear.equals(birthDateMonth);
    }

    /**
     * Writes a list of customers as a JSON String to file.
     *
     * @param customerList list of customers to be written to a file as JSON.
     * @param fileName     the name of the file that is written to.
     * @return list of customers that were written to file.
     */
    public static List<CustomerDto> writeToExportFile(final @NotNull List<CustomerDto> customerList,
                                                      final @NotBlank String fileName) {
        if (!isEmpty(customerList)) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
                bufferedWriter.write(convertListToJSON(customerList));
            } catch (IOException e) {
                log.error("An error has occurred while exporting birthday customer and sims:{}", e.getMessage());
                return new ArrayList<>();
            }
        }
        return customerList;
    }

    /**
     * Converts a list object to a JSON String
     *
     * @param birthDateFilteredCustomers list object
     * @return JSON representation of the object
     */
    public static String convertListToJSON(List<CustomerDto> birthDateFilteredCustomers) {
        try {
            return getObjectMapper().writeValueAsString(birthDateFilteredCustomers);
        } catch (JsonProcessingException e) {
            log.error("Error while converting the customer list to JSON:{}", e.getMessage());
        }
        return "";
    }

    public static ObjectMapper getObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        DATE_FORMAT.setTimeZone(TIME_ZONE);
        return mapper.setDateFormat(DATE_FORMAT);
    }
}
