package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProdutoDto {
    private Long id;
    private String nome;
    private byte[] imagem;
}
