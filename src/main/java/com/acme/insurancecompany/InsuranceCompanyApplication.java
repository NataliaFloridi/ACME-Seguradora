package com.acme.insurancecompany;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
public class InsuranceCompanyApplication {

    public static void main(String[] args) {
        SpringApplication.run(InsuranceCompanyApplication.class, args);
    }
} 