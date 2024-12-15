package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResOfertaCorrida {
    private String enderecoInicial;
    private String enderecoFinal;
    private String valorCorrida;
}
