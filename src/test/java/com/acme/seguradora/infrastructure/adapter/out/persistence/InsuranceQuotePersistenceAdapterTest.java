package com.acme.seguradora.infrastructure.adapter.out.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import com.acme.insurancecompany.application.mapper.InsuranceQuoteMapper;
import com.acme.insurancecompany.domain.exception.QuotePersistenceException;
import com.acme.insurancecompany.domain.exception.ResourceNotFoundException;
import com.acme.insurancecompany.domain.model.InsuranceQuote;
import com.acme.insurancecompany.infrastructure.adapter.out.persistence.adapter.insurancequote.InsuranceQuotePersistenceAdapter;
import com.acme.insurancecompany.infrastructure.adapter.out.persistence.entity.InsuranceQuoteEntity;
import com.acme.insurancecompany.infrastructure.adapter.out.persistence.repository.InsuranceQuoteRepository;

@ExtendWith(MockitoExtension.class)
public class InsuranceQuotePersistenceAdapterTest {
    
    @Mock
    private InsuranceQuoteRepository insuranceQuoteRepository;

    @Mock
    private InsuranceQuoteMapper insuranceQuoteMapper;

    @InjectMocks
    private InsuranceQuotePersistenceAdapter insuranceQuotePersistenceAdapter;
    
    @Test
    void testSaveInsuranceQuote() {
        InsuranceQuoteEntity insuranceQuoteEntity = InsuranceQuoteEntity.builder()
            .build();
        InsuranceQuote insuranceQuote = InsuranceQuote.builder()
            .build();
            
        when(insuranceQuoteMapper.toEntity(insuranceQuote)).thenReturn(insuranceQuoteEntity);
        when(insuranceQuoteRepository.save(any())).thenReturn(insuranceQuoteEntity);
       
        InsuranceQuote result = insuranceQuotePersistenceAdapter.saveInsuranceQuote(insuranceQuote);

        assertEquals(insuranceQuote, result);

        verify(insuranceQuoteRepository, times(1)).save(any());
        verify(insuranceQuoteMapper, times(1)).toEntity(insuranceQuote);
    }

    @Test
    void testSaveInsuranceQuote_QuotePersistenceException() {
        InsuranceQuoteEntity insuranceQuoteEntity = InsuranceQuoteEntity.builder()
            .build();
        InsuranceQuote insuranceQuote = InsuranceQuote.builder()
            .build();
            
        when(insuranceQuoteMapper.toEntity(insuranceQuote)).thenReturn(insuranceQuoteEntity);
        when(insuranceQuoteRepository.save(any())).thenThrow(new QuotePersistenceException("Não foi possível salvar a cotação"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            insuranceQuotePersistenceAdapter.saveInsuranceQuote(insuranceQuote);
        });

        assertEquals("Não foi possível salvar a cotação", exception.getMessage());

        verify(insuranceQuoteRepository, times(1)).save(any());
        verify(insuranceQuoteMapper, times(1)).toEntity(insuranceQuote);
    }

    @Test
    void testSaveInsuranceQuote_ResourceNotFoundException() {
        InsuranceQuoteEntity insuranceQuoteEntity = InsuranceQuoteEntity.builder()
            .build();
        InsuranceQuote insuranceQuote = InsuranceQuote.builder()
            .build();
            
        when(insuranceQuoteMapper.toEntity(insuranceQuote)).thenReturn(insuranceQuoteEntity);
        when(insuranceQuoteRepository.save(any())).thenThrow(new ResourceNotFoundException("Recurso não encontrado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            insuranceQuotePersistenceAdapter.saveInsuranceQuote(insuranceQuote);
        });

        assertEquals("Recurso não encontrado", exception.getMessage());

        verify(insuranceQuoteRepository, times(1)).save(any());
        verify(insuranceQuoteMapper, times(1)).toEntity(insuranceQuote);
    }

    @Test
    void testCreateInsuranceQuote() {
        InsuranceQuoteEntity insuranceQuoteEntity = InsuranceQuoteEntity.builder()
            .build();
        InsuranceQuote insuranceQuote = InsuranceQuote.builder()
            .build();
            
        when(insuranceQuoteMapper.toEntity(insuranceQuote)).thenReturn(insuranceQuoteEntity);
        when(insuranceQuoteRepository.save(any())).thenReturn(insuranceQuoteEntity);

        InsuranceQuote result = insuranceQuotePersistenceAdapter.saveInsuranceQuote(insuranceQuote);

        assertEquals(insuranceQuote, result);

        verify(insuranceQuoteRepository, times(1)).save(any());
        verify(insuranceQuoteMapper, times(1)).toEntity(insuranceQuote);
    }

    @Test
    void testCreateInsuranceQuote_QuotePersistenceException() {
        InsuranceQuoteEntity insuranceQuoteEntity = InsuranceQuoteEntity.builder()
            .build();
        InsuranceQuote insuranceQuote = InsuranceQuote.builder()
            .build();

        when(insuranceQuoteMapper.toEntity(insuranceQuote)).thenReturn(insuranceQuoteEntity);
        when(insuranceQuoteRepository.save(any())).thenThrow(new QuotePersistenceException("Não foi possível criar a cotação"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            insuranceQuotePersistenceAdapter.saveInsuranceQuote(insuranceQuote);
        });

        assertEquals("Não foi possível criar a cotação", exception.getMessage());
        
        verify(insuranceQuoteRepository, times(1)).save(any());
        verify(insuranceQuoteMapper, times(1)).toEntity(insuranceQuote);
    }

    @Test
    void testCreateInsuranceQuote_ResourceNotFoundException() {
        InsuranceQuoteEntity insuranceQuoteEntity = InsuranceQuoteEntity.builder()
            .build();
        InsuranceQuote insuranceQuote = InsuranceQuote.builder()
            .build();

        when(insuranceQuoteMapper.toEntity(insuranceQuote)).thenReturn(insuranceQuoteEntity);
        when(insuranceQuoteRepository.save(any())).thenThrow(new ResourceNotFoundException("Recurso não encontrado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            insuranceQuotePersistenceAdapter.saveInsuranceQuote(insuranceQuote);
        });

        assertEquals("Recurso não encontrado", exception.getMessage());
        
        verify(insuranceQuoteRepository, times(1)).save(any());
        verify(insuranceQuoteMapper, times(1)).toEntity(insuranceQuote);
    }

    @Test
    void testFindInsuranceQuoteById() {
        InsuranceQuoteEntity insuranceQuoteEntity = InsuranceQuoteEntity.builder()
            .build();
        InsuranceQuote insuranceQuote = InsuranceQuote.builder()
            .build();
            
        // when(insuranceQuoteRepository.findById(any())).thenReturn(Optional.of(insuranceQuoteEntity));
        when(insuranceQuoteMapper.toDomain(insuranceQuoteEntity)).thenReturn(insuranceQuote);

        InsuranceQuote result = insuranceQuotePersistenceAdapter.findInsuranceQuoteById(1L);

        assertEquals(insuranceQuote, result);

        verify(insuranceQuoteRepository, times(1)).findById(1L);
        verify(insuranceQuoteMapper, times(1)).toDomain(insuranceQuoteEntity);
    }

    @Test
    void testFindInsuranceQuoteById_QuotePersistenceException() {
        when(insuranceQuoteRepository.findById(anyLong())).thenThrow(new QuotePersistenceException("Não foi possível buscar a cotação"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            insuranceQuotePersistenceAdapter.findInsuranceQuoteById(1L);
        });

        assertEquals("Não foi possível buscar a cotação", exception.getMessage());

        verify(insuranceQuoteRepository, times(1)).findById(1L);
    }

    @Test
    void testFindInsuranceQuoteById_ResourceNotFoundException() {
        when(insuranceQuoteRepository.findById(anyLong())).thenThrow(new ResourceNotFoundException("Recurso não encontrado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            insuranceQuotePersistenceAdapter.findInsuranceQuoteById(1L);
        });

        assertEquals("Recurso não encontrado", exception.getMessage());

        verify(insuranceQuoteRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateInsuranceQuoteWithPolicyId() {
        InsuranceQuoteEntity insuranceQuoteEntity = InsuranceQuoteEntity.builder()
            .build();
        InsuranceQuote insuranceQuote = InsuranceQuote.builder()
            .build();
        
        //when(insuranceQuoteRepository.findById(anyLong())).thenReturn(Optional.of(insuranceQuoteEntity));
        when(insuranceQuoteMapper.toEntity(insuranceQuote)).thenReturn(insuranceQuoteEntity);
        when(insuranceQuoteRepository.save(any())).thenReturn(insuranceQuoteEntity);

        InsuranceQuote result = insuranceQuotePersistenceAdapter.updateInsuranceQuoteWithPolicyId(1L, 1L);

        assertEquals(insuranceQuote, result);

        verify(insuranceQuoteRepository, times(1)).save(any());
        verify(insuranceQuoteMapper, times(1)).toEntity(insuranceQuote);
        verify(insuranceQuoteRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateInsuranceQuoteWithPolicyId_QuotePersistenceException() {
        when(insuranceQuoteRepository.save(any())).thenThrow(new QuotePersistenceException("Não foi possível atualizar a cotação"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            insuranceQuotePersistenceAdapter.updateInsuranceQuoteWithPolicyId(1L, 1L);
        });

        assertEquals("Erro ao atualizar cotação com ID da apólice", exception.getMessage());

        verify(insuranceQuoteRepository, times(1)).save(any());
        verify(insuranceQuoteMapper, times(1)).toEntity(any(InsuranceQuote.class));
    }

    @Test
    void testUpdateInsuranceQuoteWithPolicyId_DataAccessException() {
        when(insuranceQuoteRepository.save(any())).thenThrow(new DataAccessException("Não foi possível atualizar a cotação com ID da apólice") {});

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            insuranceQuotePersistenceAdapter.updateInsuranceQuoteWithPolicyId(1L, 1L);
        });

        assertEquals("Não foi possível atualizar a cotação", exception.getMessage());

        verify(insuranceQuoteRepository, times(1)).save(any());
        verify(insuranceQuoteMapper, times(1)).toEntity(any(InsuranceQuote.class));
    }
}