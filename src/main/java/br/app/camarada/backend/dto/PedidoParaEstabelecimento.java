package br.app.camarada.backend.dto;

import br.app.camarada.backend.enums.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PedidoParaEstabelecimento {
    private LocalDateTime horaDoPedido;
    private Long idDoProduto;
    private LocalDateTime horaDoAceiteDoPedido;
    private LocalDateTime horaDaEntregaDoPedido;
    private LocalDateTime horaDoPreparoDoPedido;
    private Integer estimativaDaDuracaoDoPedido;
    private StatusPedido estado;
}
