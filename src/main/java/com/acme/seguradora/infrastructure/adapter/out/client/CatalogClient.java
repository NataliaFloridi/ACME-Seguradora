package com.acme.seguradora.infrastructure.adapter.out.client;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.acme.seguradora.infrastructure.adapter.out.client.response.OfferResponse;
import com.acme.seguradora.infrastructure.adapter.out.client.response.ProductResponse;

@FeignClient(name = "catalog-service", url = "${services.catalog.url}")
public interface CatalogClient {

    @Cacheable(value = "products", key = "#p0")
    @GetMapping("/api/catalog/produtos/{productId}")
    ProductResponse getProduct(@PathVariable("productId") String productId);

    @Cacheable(value = "offers", key = "#p0")
    @GetMapping("/api/catalog/ofertas/{offerId}")
    OfferResponse getOffer(@PathVariable("offerId") String offerId);
} 