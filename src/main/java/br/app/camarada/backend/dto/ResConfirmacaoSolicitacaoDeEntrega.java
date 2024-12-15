package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResConfirmacaoSolicitacaoDeEntrega {
    private String distancia;
    private String valorTotal;
    private String enderecoFinal;
    private String enderecoInicial;
    private BigDecimal valor;
}
