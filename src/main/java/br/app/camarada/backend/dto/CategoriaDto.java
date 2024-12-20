package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoriaDto {
    private String nome;
    private String categoriaEnum;
}
