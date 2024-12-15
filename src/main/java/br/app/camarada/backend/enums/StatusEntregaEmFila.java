package br.app.camarada.backend.enums;

public enum StatusEntregaEmFila {
    DISPONIVEL("Aguardando atendimento")
    ,CONGELADO("Suspenso"),CANCELADO("Cancelado"), EM_NEGOCIACAO("Aguardando atendimento"), ATENDIDO("Em atendimento"), CONCLUIDA("Concluída");

    private String status;
    StatusEntregaEmFila(String status) {
        this.status=status;
    }

    public String obterStatus(){
        return this.status;
    }
}
