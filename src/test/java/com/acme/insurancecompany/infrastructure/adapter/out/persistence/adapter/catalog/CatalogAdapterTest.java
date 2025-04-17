package com.acme.insurancecompany.infrastructure.adapter.out.persistence.adapter.catalog;

import com.acme.insurancecompany.domain.exception.ResourceNotFoundException;
import com.acme.insurancecompany.domain.exception.ValidationException;
import com.acme.insurancecompany.infrastructure.adapter.out.client.CatalogClient;
import com.acme.insurancecompany.infrastructure.adapter.out.client.response.OfferResponse;
import com.acme.insurancecompany.infrastructure.adapter.out.client.response.ProductResponse;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogAdapterTest {

    @Mock
    private CatalogClient catalogClient;

    @InjectMocks
    private CatalogAdapter catalogAdapter;

    private String productId;
    private String offerId;
    private ProductResponse productResponse;
    private OfferResponse offerResponse;

    @BeforeEach
    void setUp() {
        productId = "PROD-001";
        offerId = "OFFER-001";
        productResponse = new ProductResponse();
        offerResponse = new OfferResponse();
    }

    @Test
    void shouldFindProductById() {
        when(catalogClient.getProduct(any())).thenReturn(productResponse);
 
        ProductResponse result = catalogAdapter.getProduct(productId);

        assertThat(result).isNotNull().isEqualTo(productResponse);
        verify(catalogClient, times(1)).getProduct(any());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenProductDoesNotExist() {
        when(catalogClient.getProduct(any())).thenThrow(FeignException.NotFound.class);

        assertThatThrownBy(() -> catalogAdapter.getProduct(productId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Produto não encontrado");

        verify(catalogClient, times(1)).getProduct(any());
    }

    @Test
    void shouldThrowValidationExceptionWhenProductServiceFails() {
        when(catalogClient.getProduct(any())).thenThrow(FeignException.class);

        assertThatThrownBy(() -> catalogAdapter.getProduct(productId))
            .isInstanceOf(ValidationException.class);

        verify(catalogClient, times(1)).getProduct(any());
    }

    @Test
    void shouldFindOfferById() {
        when(catalogClient.getOffer(any())).thenReturn(offerResponse);

        OfferResponse result = catalogAdapter.getOffer(offerId);

        assertThat(result).isNotNull().isEqualTo(offerResponse);
        verify(catalogClient, times(1)).getOffer(any());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenOfferDoesNotExist() {
        when(catalogClient.getOffer(any())).thenThrow(FeignException.NotFound.class);

        assertThatThrownBy(() -> catalogAdapter.getOffer(offerId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Oferta não encontrada");

        verify(catalogClient, times(1)).getOffer(any());
    }

    @Test
    void shouldThrowValidationExceptionWhenOfferServiceFails() {
        when(catalogClient.getOffer(any())).thenThrow(FeignException.class);

        assertThatThrownBy(() -> catalogAdapter.getOffer(offerId))
            .isInstanceOf(ValidationException.class);

        verify(catalogClient, times(1)).getOffer(any());
    }
} 