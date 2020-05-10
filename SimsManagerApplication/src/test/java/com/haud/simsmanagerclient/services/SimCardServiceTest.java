package com.haud.simsmanagerclient.services;

import com.haud.simsmanagerclient.dto.CustomerCreateDto;
import com.haud.simsmanagerclient.dto.SimCardCreateDto;
import com.haud.simsmanagerclient.dto.SimCardDto;
import com.haud.simsmanagerclient.entities.Customer;
import com.haud.simsmanagerclient.entities.SimCard;
import com.haud.simsmanagerclient.exception.CustomerOperationException;
import com.haud.simsmanagerclient.exception.SimCardManagerException;
import com.haud.simsmanagerclient.repositories.CustomerRepository;
import com.haud.simsmanagerclient.repositories.SimCardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.haud.simsmanagerclient.helper.Utils.createRandomCustomer;
import static com.haud.simsmanagerclient.helper.Utils.createRandomSimCard;
import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SimCardServiceTest {

    @Mock private CustomerService customerService;
    @Mock private CustomerRepository customerRepository;
    @Mock private SimCardRepository simCardRepository;

    @InjectMocks private SimCardService simCardService;

    @Test
    public void test_whenCreateSimCard_returnsASimCardDto() {
        final SimCardCreateDto simCardCreateDto = createRandomSimCard(true, 0);
        final SimCard simCard = SimCardCreateDto.transform(simCardCreateDto);
        simCard.setId(1L); // otherwise the transform will give us an object without the id.
        lenient().when(simCardRepository.save(any(SimCard.class))).thenReturn(simCard);
        assertEquals(SimCardDto.transform(simCard), simCardService.createSimCard(simCardCreateDto));
    }

    @Test
    public void test_whenCreateSimCard_simCardIsNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> simCardService.createSimCard(null));
    }

    @Test
    public void test_whenCreateCustomer_customerIsEmpty_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> simCardService.createSimCard(new SimCardCreateDto()) );
    }

    @Test void test_whenLinkingSimToCustomer_shouldReturnOkMessage() throws SimCardManagerException {
        final Customer customer = CustomerCreateDto.transform(createRandomCustomer(0));
        customer.setId(1L); // otherwise the transform will give us an object without the id.
        when(customerService.findCustomerById(1L)).thenReturn(customer);

        final SimCardCreateDto simCardCreateDto = createRandomSimCard(false, 0);
        final SimCard simCard = SimCardDto.transform(simCardCreateDto);
        simCard.setId(1L); // otherwise the transform will give us an object without the id.

        when(simCardRepository.findById(1L)).thenReturn(Optional.of(simCard));
        when(simCardRepository.save(any(SimCard.class))).thenReturn(simCard);
        final String simCardLinkedWithCustomerMessage = format(SimCardService.SIMCARD_LINK_TO_CUSTOMER, 1L, 1L);
        assertEquals(simCardLinkedWithCustomerMessage, simCardService.linkSimCardToCustomer(1L, 1L));
    }

    @Test void test_whenReturningCustomerSims_shouldReturnAListOfSims() throws CustomerOperationException {
        final SimCardCreateDto simCardCreateDto = createRandomSimCard(true, 0);
        final SimCard simCard = SimCardDto.transform(simCardCreateDto);
        simCard.setId(1L);

        when(simCardRepository.findByCustomerId(1L)).thenReturn(singletonList(simCard));
        assertEquals(singletonList(SimCardDto.transform(simCard)), simCardService.findSimCardsByCustomerId(1L));
    }

    @Test void test_whenReturningAllSims_shouldReturnAListOfSims() throws CustomerOperationException {
        final SimCardCreateDto simCardCreateDto1 = createRandomSimCard(true, 0);
        final SimCard simCard1 = SimCardDto.transform(simCardCreateDto1);
        simCard1.setId(1L);

        final SimCardCreateDto simCardCreateDto2 = createRandomSimCard(false, 0);
        final SimCard simCard2 = SimCardDto.transform(simCardCreateDto2);
        simCard2.setId(2L);

        when(simCardRepository.findAll()).thenReturn(Arrays.asList(simCard1, simCard2));
        assertEquals(Arrays.asList(SimCardDto.transform(simCard1), SimCardDto.transform(simCard2)),
                simCardService.findAllSims());
    }

}
