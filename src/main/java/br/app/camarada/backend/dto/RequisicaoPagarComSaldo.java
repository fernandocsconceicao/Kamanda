package br.app.camarada.backend.dto;


import br.app.camarada.backend.enums.TipoServico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequisicaoPagarComSaldo {
    private BigDecimal valor;
    private TipoServico tipoServico;

}
