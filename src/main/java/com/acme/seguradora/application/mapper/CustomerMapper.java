package com.acme.seguradora.application.mapper;

import org.springframework.stereotype.Component;

import com.acme.seguradora.application.dto.CustomerRequest;
import com.acme.seguradora.application.dto.CustomerResponse;
import com.acme.seguradora.domain.model.Customer;

@Component
public class CustomerMapper {
    
    public static final CustomerMapper INSTANCE = new CustomerMapper();
    
    private CustomerMapper() {}
    
    public Customer toDomain(CustomerRequest request) {
        return Customer.builder()
                .documentNumber(request.getDocumentNumber())
                .name(request.getName())
                .type(request.getType())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .build();
    }
    
    public CustomerResponse toResponse(Customer domain) {
        return CustomerResponse.builder()
                .documentNumber(domain.getDocumentNumber())
                .name(domain.getName())
                .type(domain.getType())
                .gender(domain.getGender())
                .dateOfBirth(domain.getDateOfBirth())
                .email(domain.getEmail())
                .phoneNumber(domain.getPhoneNumber())
                .build();
    }
} 