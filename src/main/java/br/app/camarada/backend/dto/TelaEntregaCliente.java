package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class TelaEntregaCliente {
    private EnderecoDto endereco;
    private List<ComponenteDoPedido> componentes;
    private BigDecimal valorTotal;
    private String valorTotalFormatado;
}
