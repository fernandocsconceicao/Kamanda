package br.app.camarada.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class TelaDePedidosParaClientes {
    private List<PedidoDto> pedidos;
}
