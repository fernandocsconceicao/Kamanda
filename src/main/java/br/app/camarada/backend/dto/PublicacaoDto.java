package br.app.camarada.backend.dto;

import br.app.camarada.backend.enums.CategoriaPublicacao;
import br.app.camarada.backend.enums.TipoPublicacao;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PublicacaoDto {
    private Long id;
    private TipoPublicacao tipoPublicacao;
    private PerfilPublicacaoDto autorPrincipal;
    private String resumo;
    private String data;
    private String texto;
    private byte[] imagem;
    private String manchete;
    private byte[] miniatura;
    private Integer visualizacoes;
    private CategoriaPublicacao categoria;
}
