package br.app.camarada.backend.dto;

import br.app.camarada.backend.enums.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PedidoDto {
    private Long id;
    private byte [] miniatura;
    private String nome;
    private String valor;
    private String statusTexto;
    private StatusPedido status;
    //    val orderTime: String,
}
