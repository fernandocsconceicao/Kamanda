package br.app.camarada.backend.enums;

public enum StatusEntregador {
    INATIVO("Inativo"),
    OFFLINE("Offline"),
    ONLINE("Online"),
    TRABALHANDO("Trabalhando"),
    AVALIANDO("Requisitado"),
    EM_VIAGEM("Em viagem"),
    EXCLUIDO("Indisponivel");


private String status;
    StatusEntregador(String status) {
        this.status=status;
    }

    public String obterStatus(){
        return this.status;
    }
}
