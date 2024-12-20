package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartaoEstabelecimento {
    private String titulo;
    private String valor;
}
