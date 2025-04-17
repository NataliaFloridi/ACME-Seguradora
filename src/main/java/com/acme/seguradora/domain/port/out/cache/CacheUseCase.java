package com.acme.seguradora.domain.port.out.cache;

import com.acme.seguradora.infrastructure.adapter.out.client.response.ProductResponse;

public interface CacheUseCase {
    ProductResponse getProduct(String productId);
    void putProduct(String productId, ProductResponse product);
}