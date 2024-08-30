package br.app.camarada.backend.enums;

public enum Cabecalhos {
    USUARIO("x-usuarioId"),
    EMAIL("x-email"),
    CARRINHO("x-carrinho"),
    NOME("x-nome"),
    TIPO_CONTA("x-conta"), PREFERENCIA("x-preferencia"),
     CONTA_FINANCEIRA("x-conta-financeira");
    private final String value;

    Cabecalhos(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
