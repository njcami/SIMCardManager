package com.haud.simsmanagerclient.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haud.simsmanagerclient.dto.CustomerCreateDto;
import com.haud.simsmanagerclient.dto.CustomerDto;
import com.haud.simsmanagerclient.dto.CustomerUpdateDto;
import com.haud.simsmanagerclient.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.haud.simsmanagerclient.helper.Utils.createRandomCustomer;
import static com.haud.simsmanagerclient.helper.Utils.getObjectMapper;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomerService customerService;

    @Test
    public void givenCustomer_whenPostCustomerCreate_thenReturnCustomer() throws Exception {

        final ObjectMapper mapper = getObjectMapper();
        final CustomerCreateDto customerCreateDto = createRandomCustomer(1); // create DTO
        final CustomerDto customerDto = CustomerDto.transform(CustomerCreateDto.transform(customerCreateDto)); // expected DTO

        given(customerService.createCustomer(customerCreateDto)).willReturn(customerDto);

        mvc.perform(post("/customer")
                .content(mapper.writeValueAsString(customerCreateDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(customerDto)));
    }

    @Test
    public void givenEmptyCustomer_whenPostCustomerCreate_willHaveBadRequestStatus() throws Exception {
        mvc.perform(post("/customer")
                .content("")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
