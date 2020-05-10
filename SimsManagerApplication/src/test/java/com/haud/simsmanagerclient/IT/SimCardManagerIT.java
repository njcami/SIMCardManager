package com.example.simsmanagerclient.IT;

import com.example.simsmanagerclient.dto.CustomerDto;
import com.example.simsmanagerclient.dto.SimCardDto;
import com.example.simsmanagerclient.exception.SimCardManagerException;
import com.example.simsmanagerclient.services.CustomerService;
import com.example.simsmanagerclient.services.SimCardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.simsmanagerclient.helper.Utils.createRandomCustomer;
import static com.example.simsmanagerclient.helper.Utils.createRandomSimCard;

@SpringBootTest
public class SimCardManagerIT {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SimCardService simCardService;

    @Test
    public void initDatabaseAndDoScheduledTasks() throws SimCardManagerException {

        // create simCard and customer and link them
        final SimCardDto simCard = simCardService.createSimCard(createRandomSimCard(false, 0));
        final CustomerDto customer = customerService.createCustomer(createRandomCustomer(0));
        simCardService.linkSimCardToCustomer(simCard.getId(), customer.getId());

        // create 10 customers with birthdays from today till 10 days in the future
        for (int toBirthDay = 0; toBirthDay < 10; toBirthDay++) {
            simCardService.createSimCard(createRandomSimCard(true, toBirthDay));
        }

        // send email notification to the customer that has the birthday on the 7th day
        // need to set SMTP settings in application.yml to send email
        customerService.sendEmailNotification();

        // export all customers (2 in total from above) to a file in JSON format
        // File name: customersAndSimsExports.json
        customerService.exportBirthdayCustomersWithSimCardsToFile();
    }
}
