package com.cards.cards.controller;

import com.cards.cards.dtos.CardRequest;
import com.cards.cards.dtos.CardDetails;
import com.cards.cards.service.ICardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CardsController.class)
class CardsControllerTest {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICardService iCardService;

    private CardDetails expectedCardDetails;

    @BeforeEach
    void setUp() {
        expectedCardDetails = new CardDetails();
        expectedCardDetails.setAccountNumber(1598472658);
        expectedCardDetails.setBranchAddress("Pasing");
        expectedCardDetails.setCardType("DEBIT");
        expectedCardDetails.setDailyLimit(3);
        expectedCardDetails.setPersonalAddress("Planegger");
        expectedCardDetails.setStatus("ACTIVE");
    }

    @Test
    void happyPathToIssueCard() throws Exception {
        when(iCardService.issueCard(any(CardDetails.class))).thenReturn(expectedCardDetails);
        String url = "/cards/issue-card";
        mockMvc.perform(post(url).
                content(mapper.writeValueAsString(expectedCardDetails)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value(1598472658));
    }
    @Test
    void failedPathToIssueCard() throws Exception {
        expectedCardDetails.setBranchAddress(null);
        String url = "/cards/issue-card";
        mockMvc.perform(post(url).content(mapper.writeValueAsString(expectedCardDetails))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }
    @Test
    void happyPathToActivate() throws Exception{
        CardRequest cardRequest = new CardRequest();
        cardRequest.setCardNumber(8254965598L);
        when(iCardService.activate(any(CardRequest.class))).thenReturn(expectedCardDetails);
        String url = "/cards/activate";
         mockMvc.perform(put(url).content(mapper.writeValueAsString(cardRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value(1598472658))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("ACTIVE"));
    }
    @Test
    void failedPathToActivate() throws Exception{
        CardRequest cardRequest = new CardRequest();
        cardRequest.setCardNumber(7825495);
        String url = "/cards/activate";
        mockMvc.perform(put(url).content(mapper.writeValueAsString(cardRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void happyPathToDeactivate() throws Exception {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setCardNumber(8254965598L);
        expectedCardDetails.setStatus("INACTIVE");
        when(iCardService.deactivate(any(CardRequest.class))).thenReturn(expectedCardDetails);
        String url = "/cards/deactivate";
        mockMvc.perform(put(url).content(mapper.writeValueAsString(cardRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("INACTIVE"));
    }
    @Test
    void failedPathToDeactivate() throws Exception {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setCardNumber(82549655L);
        String url = "/cards/deactivate";
        mockMvc.perform(put(url).content(mapper.writeValueAsString(cardRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void happyPathToFetchAllCardDetails() throws Exception {
        long accountNumber = 1598472658;
        List<CardDetails> cardDetailsList = new ArrayList<>();
        cardDetailsList.add(expectedCardDetails);
        when(iCardService.fetchAllCardDetails(accountNumber)).thenReturn(cardDetailsList);
        String url = "/cards/card-details/1598472658";
        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1));
    }
    @Test
    void failedPathToFetchAllCardDetails() throws Exception {
        long accountNumber = 1598472;
        List<CardDetails> cardDetailsList = new ArrayList<>();
        cardDetailsList.add(expectedCardDetails);
        when(iCardService.fetchAllCardDetails(accountNumber)).thenReturn(cardDetailsList);
        String url = "/cards/card-details/1598472";
        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
    }

    @Test
    void happyPathToFetchCardDetailsByCardNumber() throws Exception {
        long cardNumber = 8254965598L;
        expectedCardDetails.setCardNumber(cardNumber);
        when(iCardService.fetchCardDetailsByCardNumber(cardNumber)).thenReturn(expectedCardDetails);
        String url = "/cards/card-details-by-cardNumber/8254965598";
        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cardNumber").value(8254965598L));
    }

    @Test
    void happyPathToExtendExpiredDate() throws Exception{
        CardRequest cardRequest = new CardRequest();
        cardRequest.setCardNumber(8254965598L);
        expectedCardDetails.setExpiredDate(LocalDateTime.now().plusYears(6));
        when(iCardService.extendExpiredDate(any(CardRequest.class))).thenReturn(expectedCardDetails);
        String url = "/cards/extend-expiredDate";
        mockMvc.perform(put(url).content(mapper.writeValueAsString(cardRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value(1598472658));


    }
}