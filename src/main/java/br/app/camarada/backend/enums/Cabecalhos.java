package br.app.camarada.backend.enums;

public enum Cabecalhos {
    USUARIO("x-usuarioId"),
    EMAIL("x-email"),
    NOME("x-nome"),
    TIPO_CONTA("x-conta"),
    PREFERENCIA("x-preferencia"),
     CONTA_FINANCEIRA("x-conta-financeira"),
     PERFIL("x-perfil"),
    ESTABELECIMENTO("x-estabelecimento"),
    PRIMEIRA_COMPRA("x-primeira_compra"),
    ENDERECO("x-endereco"),
    EMAIL_AUTENTICADO("x-email-autenticado");

    private final String value;

    Cabecalhos(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
