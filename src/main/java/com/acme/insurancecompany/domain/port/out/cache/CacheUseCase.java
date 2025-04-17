package com.acme.insurancecompany.domain.port.out.cache;

import com.acme.insurancecompany.infrastructure.adapter.out.client.response.ProductResponse;

public interface CacheUseCase {
    ProductResponse getProduct(String productId);
    void putProduct(String productId, ProductResponse product);
}