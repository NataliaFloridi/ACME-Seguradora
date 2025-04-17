package com.acme.insurancecompany.domain.port.out.port;

import com.acme.insurancecompany.infrastructure.adapter.out.client.response.OfferResponse;
import com.acme.insurancecompany.infrastructure.adapter.out.client.response.ProductResponse;

public interface CatalogPort {
    ProductResponse getProduct(String productId);
    OfferResponse getOffer(String offerId);
}
