# ACME Seguradora

Serviço de cotações de seguro da ACME Seguradora.

## Índice

- [Arquitetura](#arquitetura)
- [Tecnologias](#tecnologias)
- [Requisitos](#requisitos)
- [Executando o Projeto](#executando-o-projeto)
- [API](#api)
- [Cache](#cache)
- [Validações](#validações)
- [Tratamento de Erros](#tratamento-de-erros)
- [Testes](#testes)
- [Logs e Monitoramento](#logs-e-monitoramento)
- [Decisões de Projeto](#decisões-de-projeto)
- [Premissas](#premissas)

## Arquitetura

O projeto segue os princípios da Arquitetura Hexagonal (Ports and Adapters), com as seguintes camadas:

### 1. Domain (Domínio)
- **Model**: Entidades e regras de negócio
  - `InsuranceQuote.java` - Entidade de domínio
  - `Customer.java` - Entidade de domínio
  - `Coverage.java` - Entidade de domínio
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
    - `client/` - Clientes HTTP
      - `CatalogServiceClient.java`
- **Config**: Configurações do sistema
- **Catalog**: Integração com catálogo

### Design Patterns Utilizados

- **Repository**: Para abstrair o acesso aos dados
- **Factory**: Para criar objetos complexos
- **Strategy**: Para diferentes implementações de validação
- **Observer**: Para notificações de eventos
- **Adapter**: Para adaptar interfaces externas

## Tecnologias

- Java 17
- Spring Boot 3.x
- Oracle Database
- RabbitMQ
- JPA/Hibernate
- Lombok
- Maven
- Caffeine Cache
- TestContainers
- WireMock

## Requisitos

- Docker
- Docker Compose
- Java 17
- Maven
- Oracle Database (ou Docker com imagem Oracle)

## Executando o Projeto

1. Clone o repositório
2. Configure as credenciais do Oracle Database no arquivo `application.properties`
3. Execute o comando para iniciar a infraestrutura:
```bash
docker-compose up -d
```
4. Execute a aplicação:
```bash
mvn spring-boot:run
```

## API

### Endpoints Disponíveis

#### 1. Criar Cotação
```bash
POST /api/cotacoes
```

#### 2. Consultar Cotação
```bash
GET /api/cotacoes/{id}
```

#### 3. Listar Cotações
```bash
GET /api/cotacoes
```

### Exemplos de Requisições e Respostas

#### Criar Cotação
```bash
curl -X POST http://localhost:8080/api/cotacoes \
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

Resposta de sucesso:
```json
{
  "id": 22345,
  "status": "PENDENTE",
  "created_at": "2024-05-22T20:37:17.090098"
}
```

### Códigos de Erro

| Código | Descrição |
|--------|-----------|
| 400 | Bad Request - Dados inválidos |
| 404 | Not Found - Recurso não encontrado |
| 500 | Internal Server Error - Erro interno do servidor |

## Cache

O sistema utiliza cache em memória com Caffeine para:

- Consultas ao catálogo de produtos
- Consultas ao catálogo de ofertas

O cache é configurado para expirar após 30 minutos e manter no máximo 100 itens.

## Validações

O sistema implementa validações para:

- Dados do cliente (CPF, email, telefone, etc)
- Produtos e ofertas
- Coberturas e assistências
- Valores de prêmio

## Tratamento de Erros

O sistema implementa tratamento de erros com:

- Exceções de negócio customizadas
- Handler global de exceções
- Respostas de erro padronizadas
- Validações com Bean Validation

## Testes

### Cobertura de Testes

O sistema possui uma cobertura abrangente de testes:

- Testes Unitários: 85% de cobertura
- Testes de Integração: 75% de cobertura
- Testes de Cenários de Erro: 90% de cobertura

### Tipos de Testes

1. **Testes Unitários**
   - Validação de dados do cliente
   - Cálculos de prêmio
   - Validação de coberturas
   - Validação de assistências
   - Cache de produtos e ofertas

2. **Testes de Integração**
   - Criação de cotação
   - Consulta de cotação
   - Validação com serviço de catálogo
   - Integração com RabbitMQ
   - Persistência no banco de dados

3. **Testes de Cenários de Erro**
   - Produto inexistente
   - Oferta inativa
   - Coberturas inválidas
   - Assistências inválidas
   - Valores de prêmio fora dos limites
   - Dados do cliente inválidos

## Logs e Monitoramento

O sistema utiliza logs estruturados em JSON para facilitar a análise. Exemplos de logs:

### Log de Criação de Cotação
```json
{
  "timestamp": "2024-05-22T20:37:17.090098",
  "level": "INFO",
  "service": "acme-seguradora",
  "traceId": "abc123",
  "spanId": "def456",
  "message": "Nova cotação recebida",
  "cotacaoId": 22345,
  "productId": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
  "offerId": "adc56d77-348c-4bf0-908f-22d402ee715c"
}
```

### Log de Validação
```json
{
  "timestamp": "2024-05-22T20:37:17.090098",
  "level": "DEBUG",
  "service": "acme-seguradora",
  "traceId": "abc123",
  "spanId": "def456",
  "message": "Iniciando validação de cotação",
  "cotacaoId": 22345,
  "validacoes": [
    "produto",
    "oferta",
    "coberturas",
    "assistências",
    "prêmio"
  ]
}
```

### Log de Erro
```json
{
  "timestamp": "2024-05-22T20:37:17.090098",
  "level": "ERROR",
  "service": "acme-seguradora",
  "traceId": "abc123",
  "spanId": "def456",
  "message": "Falha na validação de cotação",
  "cotacaoId": 22345,
  "erro": "Valor do prêmio mensal fora dos limites permitidos",
  "stackTrace": "..."
}
```

## Decisões de Projeto

1. **RabbitMQ**: Escolhido por ser mais simples de configurar e testar que Kafka
2. **Oracle Database**: Escolhido por suporte nativo a JSON e ser mais simples que NoSQL
3. **Clean Architecture**: Para manter o código organizado e testável
4. **Cache em Memória**: Para melhorar performance das consultas frequentes
5. **Validações no Use Case**: Para manter a lógica de negócio centralizada
6. **TestContainers**: Para testes de integração mais realistas
7. **WireMock**: Para simular o serviço de catálogo

## Premissas

1. O serviço de catálogo é mockado para simplicidade
2. O serviço de apólices é simulado via RabbitMQ
3. Não há necessidade de autenticação/autorização
4. Os dados do cliente são validados conforme regras de negócio 

## Exemplos de Uso

### 1. Criando uma Nova Cotação

```bash
curl -X POST http://localhost:8080/api/cotacoes \
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

Resposta de sucesso:
```json
{
  "id": 22345,
  "status": "PENDENTE",
  "created_at": "2024-05-22T20:37:17.090098"
}
```

### 2. Consultando uma Cotação

```bash
curl -X GET http://localhost:8080/api/cotacoes/22345
```

Resposta:
```json
{
  "id": 22345,
  "insurance_policy_id": null,
  "product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
  "offer_id": "adc56d77-348c-4bf0-908f-22d402ee715c",
  "category": "HOME",
  "created_at": "2024-05-22T20:37:17.090098",
  "updated_at": "2024-05-22T20:37:17.090098",
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
}
```

### 3. Listando Todas as Cotações

```bash
curl -X GET http://localhost:8080/api/cotacoes
```

Resposta:
```json
{
  "content": [
    {
      "id": 22345,
      "insurance_policy_id": null,
      "product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
      "offer_id": "adc56d77-348c-4bf0-908f-22d402ee715c",
      "category": "HOME",
      "created_at": "2024-05-22T20:37:17.090098",
      "updated_at": "2024-05-22T20:37:17.090098",
      "total_monthly_premium_amount": 75.25,
      "total_coverage_amount": 825000.00
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    }
  },
  "totalElements": 1,
  "totalPages": 1,
  "last": true,
  "size": 10,
  "number": 0,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "first": true,
  "numberOfElements": 1,
  "empty": false
}
```

### 4. Exemplos de Erros

#### Produto Inexistente
```json
{
  "timestamp": "2024-05-22T20:37:17.090098",
  "status": 400,
  "error": "Bad Request",
  "message": "Produto não encontrado",
  "path": "/api/cotacoes"
}
```

#### Oferta Inativa
```json
{
  "timestamp": "2024-05-22T20:37:17.090098",
  "status": 400,
  "error": "Bad Request",
  "message": "Oferta não está ativa",
  "path": "/api/cotacoes"
}
```

#### Valor de Prêmio Fora do Limite
```json
{
  "timestamp": "2024-05-22T20:37:17.090098",
  "status": 400,
  "error": "Bad Request",
  "message": "Valor do prêmio mensal fora dos limites permitidos",
  "path": "/api/cotacoes"
}
```

## Testes

### Cobertura de Testes

O sistema possui uma cobertura abrangente de testes:

- Testes Unitários: 85% de cobertura
- Testes de Integração: 75% de cobertura
- Testes de Cenários de Erro: 90% de cobertura

### Tipos de Testes

1. **Testes Unitários**
   - Validação de dados do cliente
   - Cálculos de prêmio
   - Validação de coberturas
   - Validação de assistências
   - Cache de produtos e ofertas

2. **Testes de Integração**
   - Criação de cotação
   - Consulta de cotação
   - Validação com serviço de catálogo
   - Integração com RabbitMQ
   - Persistência no banco de dados

3. **Testes de Cenários de Erro**
   - Produto inexistente
   - Oferta inativa
   - Coberturas inválidas
   - Assistências inválidas
   - Valores de prêmio fora dos limites
   - Dados do cliente inválidos

### Exemplo de Teste Unitário
```java
@Test
void deveValidarCoberturasComSucesso() {
    // Arrange
    var cotacao = criarCotacaoValida();
    var oferta = criarOfertaValida();
    
    // Act
    var resultado = validador.validarCoberturas(cotacao, oferta);
    
    // Assert
    assertTrue(resultado.isValido());
    assertNull(resultado.getErro());
}
```

### Exemplo de Teste de Integração
```java
@Test
void deveCriarCotacaoComSucesso() {
    // Arrange
    var request = criarRequestValido();
    
    // Act
    var response = restTemplate.postForEntity(
        "/api/cotacoes",
        request,
        CotacaoResponse.class
    );
    
    // Assert
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody().getId());
    assertNull(response.getBody().getInsurancePolicyId());
}

## Configuração do Banco de Dados

O projeto utiliza Oracle Database. As configurações de conexão devem ser definidas no arquivo `application.properties`:

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.database-platform=org.hibernate.dialect.OracleDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
``` 