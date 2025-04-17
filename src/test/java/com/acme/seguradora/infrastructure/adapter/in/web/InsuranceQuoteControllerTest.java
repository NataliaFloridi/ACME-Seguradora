package com.acme.seguradora.infrastructure.adapter.in.web;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.acme.insurancecompany.application.dto.InsuranceQuoteRequest;
import com.acme.insurancecompany.application.dto.InsuranceQuoteResponse;
import com.acme.insurancecompany.application.mapper.InsuranceQuoteMapper;
import com.acme.insurancecompany.domain.model.InsuranceQuote;
import com.acme.insurancecompany.domain.port.in.usecase.insurancequote.CreateInsuranceQuoteUseCase;
import com.acme.insurancecompany.domain.port.in.usecase.insurancequote.FindInsuranceQuoteUseCase;
import com.acme.insurancecompany.domain.validator.InsuranceQuoteValidator;
import com.acme.insurancecompany.infrastructure.adapter.in.web.InsuranceQuoteController;

@ExtendWith(MockitoExtension.class)
public class InsuranceQuoteControllerTest {

    @Mock
    private CreateInsuranceQuoteUseCase createInsuranceQuoteUseCase;

    @Mock
    private FindInsuranceQuoteUseCase findInsuranceQuoteUseCase;

    @Mock
    private InsuranceQuoteMapper insuranceQuoteMapper;
    
    @Mock
    private InsuranceQuoteValidator insuranceQuoteValidation;

    @InjectMocks
    private InsuranceQuoteController insuranceQuoteController;

    @Test
    void testCreateInsuranceQuote() {
        InsuranceQuoteRequest request = InsuranceQuoteRequest.builder().build();
        InsuranceQuoteResponse response = InsuranceQuoteResponse.builder().build();
        
        when(createInsuranceQuoteUseCase.execute(any())).thenReturn(new InsuranceQuote());
        when(insuranceQuoteMapper.toResponse(any(InsuranceQuote.class))).thenReturn(response);

        ResponseEntity<InsuranceQuoteResponse> result = insuranceQuoteController.createInsuranceQuote(request);

        assertEquals(response, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(createInsuranceQuoteUseCase, times(1)).execute(any(InsuranceQuote.class));
        verify(insuranceQuoteMapper, times(1)).toResponse(any(InsuranceQuote.class));
    }

    @Test
    void testFindInsuranceQuoteById() {
        InsuranceQuoteResponse response = InsuranceQuoteResponse.builder().build();

        when(findInsuranceQuoteUseCase.execute(any())).thenReturn(new InsuranceQuote());
        when(insuranceQuoteMapper.toResponse(any(InsuranceQuote.class))).thenReturn(response);

        ResponseEntity<InsuranceQuoteResponse> result = insuranceQuoteController.findInsuranceQuoteById(1L);

        assertEquals(response, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(findInsuranceQuoteUseCase, times(1)).execute(any());
        verify(insuranceQuoteMapper, times(1)).toResponse(any(InsuranceQuote.class));
    }
}