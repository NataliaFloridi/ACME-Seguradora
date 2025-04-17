package com.acme.seguradora.domain.port.out.port;

import com.acme.seguradora.infrastructure.adapter.out.client.response.OfferResponse;
import com.acme.seguradora.infrastructure.adapter.out.client.response.ProductResponse;

public interface CatalogPort {
    ProductResponse getProduct(String productId);
    OfferResponse getOffer(String offerId);
}
