package com.haud.simsmanagerclient.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haud.simsmanagerclient.dto.SimCardCreateDto;
import com.haud.simsmanagerclient.dto.SimCardDto;
import com.haud.simsmanagerclient.services.SimCardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static com.haud.simsmanagerclient.helper.Utils.createRandomSimCard;
import static com.haud.simsmanagerclient.helper.Utils.getObjectMapper;
import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SimCardController.class)
public class SimCardControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SimCardService simCardService;

    @Test
    public void givenSimCard_whenPostSimCardCreate_thenReturnSimCard() throws Exception {

        final ObjectMapper mapper = getObjectMapper();
        final SimCardCreateDto simCardCreateDto = createRandomSimCard(true, 1); // create DTO
        final SimCardDto simCardDtoWithCustomer = SimCardDto.transform(SimCardCreateDto.transform(simCardCreateDto)); // expected DTO

        given(simCardService.createSimCard(simCardCreateDto)).willReturn(simCardDtoWithCustomer);

        mvc.perform(post("/simcard")
                .content(mapper.writeValueAsString(simCardCreateDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(simCardDtoWithCustomer)));
    }

    @Test
    public void givenEmptySimCard_whenPostSimCardCreate_willHaveBadRequestStatus() throws Exception {
        mvc.perform(post("/simcard")
                .content("")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenSimCardandCustomer_whenGetLinkSimCardToCustomer_thenLinkSuccessfully() throws Exception {
        final String simCardLinkedWithCustomerMessage = format(SimCardService.SIMCARD_LINK_TO_CUSTOMER, 1L, 1L);
        given(simCardService.linkSimCardToCustomer(1L, 1L))
                .willReturn(simCardLinkedWithCustomerMessage);
        mvc.perform(get("/simcard/1/link-to-customer/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(simCardLinkedWithCustomerMessage));
    }

    @Test
    public void givenSimCardWithInCorrectIdandCustomer_whenGetLinkSimCardToCustomer_thenStatusIsBadRequest() throws Exception {
        mvc.perform(get("/simcard/0/link-to-customer/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenCustomerWithInCorrectIdandSimCardId_whenGetLinkSimCardToCustomer_thenStatusIsBadRequest() throws Exception {
        mvc.perform(get("/simcard/1/link-to-customer/0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenSimCardandCustomer_retrievesSimCardList_byCustomerId() throws Exception {
        final ObjectMapper mapper = getObjectMapper();
        final SimCardCreateDto simCardCreateDto = createRandomSimCard(false, 0); // create DTO
        final SimCardDto simCardDtoWithCustomer = SimCardDto.transform(SimCardCreateDto.transform(simCardCreateDto)); // expected DTO

        given(simCardService.findSimCardsByCustomerId(1L)).willReturn(singletonList(simCardDtoWithCustomer));
        mvc.perform(get("/simcard?customerId=1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(singletonList(simCardDtoWithCustomer))));
    }

    @Test
    public void givenInvalidCustomerId_statusIsBadRequest() throws Exception {
         mvc.perform(get("/simcard?customerId=-1"))
                .andExpect(status().isBadRequest());
    }
}
