package com.acme.insurancecompany.domain.exception;

public enum ErrorCode {
    
    PRODUTO_NAO_ENCONTRADO("PRODUTO_NAO_ENCONTRADO", "Produto não encontrado"),
    PRODUTO_INATIVO("PRODUTO_INATIVO", "Produto está inativo"),
    OFERTA_NAO_ENCONTRADA("OFERTA_NAO_ENCONTRADA", "Oferta não encontrada"),
    OFERTA_INATIVA("OFERTA_INATIVA", "Oferta está inativa"),
    OFERTA_NAO_PERTENCE_PRODUTO("OFERTA_NAO_PERTENCE_PRODUTO", "Oferta não pertence ao produto"),
    COBERTURA_INDISPONIVEL("COBERTURA_INDISPONIVEL", "Cobertura indisponível na oferta: %s"),
    ASSISTENCIA_INDISPONIVEL("ASSISTENCIA_INDISPONIVEL", "Assistência indisponível na oferta: %s"),
    VALOR_COBERTURA_EXCEDE_MAXIMO("VALOR_COBERTURA_EXCEDE_MAXIMO", "Valor da cobertura %s (%.2f) excede o valor máximo permitido (%.2f)"),
    VALOR_PREMIO_FORA_INTERVALO("VALOR_PREMIO_FORA_INTERVALO", "Valor do prêmio mensal (%.2f) está fora do intervalo permitido (%.2f - %.2f)"),
    VALOR_TOTAL_COBERTURAS_INCORRETO("VALOR_TOTAL_COBERTURAS_INCORRETO", "Valor total das coberturas (%.2f) não corresponde à soma dos valores individuais (%.2f)"),
    ERRO_AO_PROCESSAR_COTACAO("ERRO_AO_PROCESSAR_COTACAO", "Erro ao processar a cotação"),
    COTACAO_NAO_ENCONTRADA("COTACAO_NAO_ENCONTRADA", "Cotação não encontrada"),
    COTACAO_ID_VAZIO("COTACAO_ID_VAZIO", "O ID da cotação não pode ser Nulo, informe uma cotação válida"),
    ERRO_AO_BUSCAR_COTACAO_DE_SEGURO("ERRO_AO_BUSCAR_COTACAO_DE_SEGURO", "Erro ao buscar cotação de seguro"), 
    ERRO_AO_BUSCAR_OFERTA("ERRO_AO_BUSCAR_OFERTA", "Erro ao buscar oferta"), 
    ERRO_AO_BUSCAR_PRODUTO("ERRO_AO_BUSCAR_PRODUTO", "Erro ao buscar produto"), 
    ERRO_AO_ATUALIZAR_COTACAO_DE_SEGURO("ERRO_AO_ATUALIZAR_COTACAO_DE_SEGURO", "Erro ao atualizar cotação de seguro"),
    ERRO_INTERNO("ERRO_INTERNO", "Ocorreu um erro interno no servidor");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
} 