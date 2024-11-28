package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelaRecargaDeSaldo {
    private BigDecimal saldo;
    private String saldoFormatado;
}
