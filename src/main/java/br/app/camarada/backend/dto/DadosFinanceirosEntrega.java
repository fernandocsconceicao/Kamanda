package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DadosFinanceirosEntrega {
    private BigDecimal valorEntrega;
    private BigDecimal valorEstabelecimento;
    private BigDecimal valorTotal;
    private BigDecimal valorUbuntu;
}
