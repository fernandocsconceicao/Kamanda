package br.app.camarada.backend.dto;

import br.app.camarada.backend.entidades.Pedido;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class TelaDePedidosParaEstabelecimentos {
    private List<PedidoParaEstabelecimento> pedidos;

    public static TelaDePedidosParaEstabelecimentos build(List<Pedido> pedidos) {
        List<PedidoParaEstabelecimento> p = new ArrayList<>();
        pedidos.stream().forEach(
                pedido -> p.add(
                        new PedidoParaEstabelecimento(
                                pedido.getOrderTime(),
                                pedido.getId(),
                                pedido.getAcceptedTime(),
                                null,
                                null,
                                null,
                                pedido.getStatus()
                        )
                )
        );
        return TelaDePedidosParaEstabelecimentos.builder()
                .pedidos(p)
                .build();
    }
}
