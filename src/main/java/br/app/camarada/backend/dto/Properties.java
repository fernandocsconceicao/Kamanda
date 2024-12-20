package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Properties {
    private String label;
    private String value;
    private Boolean informacaoBoleana;
    private String informacao;

}
