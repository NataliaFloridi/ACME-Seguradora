# ACME Seguradora

Serviço de cotações de seguro da ACME Seguradora.

## Índice

- [Visão Geral](#visão-geral)
- [Arquitetura](#arquitetura)
- [Tecnologias](#tecnologias)
- [Requisitos](#requisitos)
- [Configuração do Ambiente](#configuração-do-ambiente)
- [Executando o Projeto](#executando-o-projeto)
- [API](#api)
- [Integração com RabbitMQ](#integração-com-rabbitmq)
- [Cache](#cache)
- [Testes](#testes)
- [Logs e Monitoramento](#logs-e-monitoramento)
- [Clean Code](#clean-code)

## Visão Geral

O projeto ACME Seguradora é um serviço de cotações de seguro que permite a criação, consulta e listagem de cotações de seguro. O sistema segue os princípios da Arquitetura Hexagonal (Ports and Adapters) e utiliza RabbitMQ para comunicação assíncrona com o serviço de apólices.

## Arquitetura

O projeto segue os princípios da Arquitetura Hexagonal (Ports and Adapters) com as seguintes camadas:

### 1. Domain (Domínio)
- **Model**: Entidades e regras de negócio
- **Port**: Interfaces para comunicação externa
  - `in/` - Ports de entrada (Primary)
    - `InsuranceQuotePort.java` - Interface para gerenciar cotações
  - `out/` - Ports de saída (Secondary)
    - `MessageBrokerPort.java` - Interface para mensageria
    - `InsuranceQuoteRepository.java` - Interface para persistência
- **Exception**: Exceções de domínio
- **Enum**: Tipos enumerados do domínio

### 2. Application (Aplicação)
- **Service**: Serviços de aplicação
  - `InsuranceQuoteService.java` - Orquestra os casos de uso
- **DTO**: Objetos de transferência
  - `InsuranceQuoteRequest.java` - DTO de entrada
  - `InsuranceQuoteResponse.java` - DTO de saída
- **Mapper**: Conversores de objetos
  - `InsuranceQuoteMapper.java` - Converte entre DTOs e entidades

### 3. Infrastructure (Infraestrutura)
- **Adapter**: Implementações dos ports
  - `in/` - Adapters de entrada (Primary)
    - `rest/` - Controllers REST
      - `InsuranceQuoteController.java`
    - `handler/` - Handlers de eventos
  - `out/` - Adapters de saída (Secondary)
    - `persistence/` - Repositórios JPA
      - `InsuranceQuotePersistenceAdapter.java`
    - `messaging/` - Integração com RabbitMQ
      - `InsurancePolicyEventListener.java`
    - `mock/` - Mock do serviço de catálogo
      - `CatalogServiceMock.java`
      - `CatalogMockServer.java`
- **Config**: Configurações do sistema

## Clean Code

O projeto segue as melhores práticas de Clean Code:

- Nomes significativos e expressivos
- Funções pequenas e com único propósito
- Comentários apenas quando necessários
- Código auto-documentado
- Padrões de formatação consistentes

## Testes

### Banco de Dados para Testes
Para testes locais e desenvolvimento, o projeto utiliza o H2 Database, um banco de dados em memória que facilita a execução dos testes sem a necessidade de um banco de dados Oracle. O H2 é configurado automaticamente durante a execução dos testes.

### Configuração do H2 para Testes
```properties
# Configuração do H2 para testes
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### Cobertura de Testes
- Cobertura de testes unitários: 85%

### Tipos de Testes
1. **Testes Unitários**
   - Testes de domínio
   - Testes de serviços
   - Testes de mappers

### Exemplos de Testes

```java
@Test
void shouldCreateInsuranceQuote() {
    // Given
    InsuranceQuoteRequest request = buildValidRequest();
    when(createInsuranceQuoteUseCase.execute(any())).thenReturn(expectedResponse);
    
    // When
    ResponseEntity<InsuranceQuoteResponse> response = controller.createInsuranceQuote(request);
    
    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isEqualTo(expectedResponse);
}
```

## Integração com RabbitMQ

O sistema utiliza RabbitMQ para comunicação assíncrona com o serviço de apólices. As mensagens são enviadas no formato JSON.

### Configuração do RabbitMQ
- Host: localhost
- Porta: 5672
- Interface de gerenciamento: http://localhost:15672
- Usuário: guest
- Senha: guest

### Exemplo de Mensagem
```json
{
  "quoteId": "12345",
  "productId": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
  "offerId": "adc56d77-348c-4bf0-908f-22d402ee715c",
  "status": "CREATED",
  "timestamp": "2024-05-22T20:37:17.090098"
}
```

### Retry Policy
- Máximo de tentativas: 3
- Intervalo entre tentativas: 5 segundos
- Backoff exponencial

## Cache

O sistema implementa cache para as consultas de produtos e ofertas do catálogo para melhorar a performance. O cache é configurado com as seguintes características:

- Cache de produtos: `products`
- Cache de ofertas: `offers`
- Tempo de expiração: 5 minutos
- Capacidade máxima: 100 itens

### Configuração do Cache
```properties
spring.cache.type=caffeine
spring.cache.caffeine.spec=maximumSize=100,expireAfterWrite=5m
```

## Tecnologias

- Java 17
- Spring Boot 3.x
- Oracle Database
- RabbitMQ
- JPA/Hibernate
- Lombok
- Maven
- Caffeine Cache
- WireMock

### Configuração do Banco de Dados
```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.database-platform=org.hibernate.dialect.OracleDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## API

### Endpoints Disponíveis

#### 1. Criar Cotação
```bash
POST /api/insurance-quotes
```

#### 2. Consultar Cotação
```bash
GET /api/insurance-quotes/{quoteId}
```

### Exemplos de Requisições

#### Criar Cotação
```bash
curl -X POST http://localhost:8080/api/insurance-quotes \
  -H "Content-Type: application/json" \
  -d '{
    "product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
    "offer_id": "adc56d77-348c-4bf0-908f-22d402ee715c",
    "category": "HOME",
    "total_monthly_premium_amount": 75.25,
    "total_coverage_amount": 825000.00,
    "coverages": {
      "Incêndio": 250000.00,
      "Desastres naturais": 500000.00,
      "Responsabiliadade civil": 75000.00
    },
    "assistances": [
      "Encanador",
      "Eletricista",
      "Chaveiro 24h"
    ],
    "customer": {
      "document_number": "36205578900",
      "name": "John Wick",
      "type": "NATURAL",
      "gender": "MALE",
      "date_of_birth": "1973-05-02",
      "email": "johnwick@gmail.com",
      "phone_number": 11950503030
    }
  }'
```

#### Consultar Cotação
```bash
curl -X GET http://localhost:8080/api/insurance-quotes/2234
```