package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SimulacaoEntregaDto {
    private BigDecimal precoEntrega;
    private Double distancia;
}
