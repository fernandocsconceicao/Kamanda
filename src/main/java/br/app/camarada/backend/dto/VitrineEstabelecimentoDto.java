package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VitrineEstabelecimentoDto {
    private Long id;
    private String nome;
    private byte[] imagem;
    private List<ProdutoDto> produtos;
}
