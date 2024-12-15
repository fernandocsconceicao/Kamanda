package br.app.camarada.backend.enums;

import lombok.Getter;

@Getter
public enum StatusPedido {
    CANCELADO("Cancelado"),
    AGUARDANDO_PAGAMENTO("Aguardando Pagamento"),
    PEDIDO("Pedido realizado"),
    ACEITO("Pedido aceito"),
    PREPARANDO("Preparando"),
    AGUARDANDO_ENTREGA("Aguardando Entrega"),
    ENTREGANDO("Em entrega"),
    RECEBIDO("Recebido"),
    ENTREGUE("Entregue"),
    CONCLUIDO("Concluído"),
    RECUSADO("Recusado"),
    AVALIADO("Avaliado");
    private String texto;

    StatusPedido(String texto) {
        this.texto = texto;
    }

}
