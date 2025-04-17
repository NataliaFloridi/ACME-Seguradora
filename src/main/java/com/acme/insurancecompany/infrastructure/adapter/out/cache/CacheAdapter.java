package com.acme.insurancecompany.infrastructure.adapter.out.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.acme.insurancecompany.domain.port.out.cache.CacheUseCase;
import com.acme.insurancecompany.infrastructure.adapter.out.client.response.ProductResponse;

@Service
public class CacheAdapter implements CacheUseCase {
    private final Map<String, ProductResponse> productCache = new ConcurrentHashMap<>();

    @Override
    public ProductResponse getProduct(String productId) {
        return productCache.get(productId);
    }

    @Override
    public void putProduct(String productId, ProductResponse product) {
        productCache.put(productId, product);
    }
} 