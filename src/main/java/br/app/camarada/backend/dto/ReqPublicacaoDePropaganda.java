package br.app.camarada.backend.dto;

import br.app.camarada.backend.entidades.Perfil;
import br.app.camarada.backend.enums.CategoriaPublicacao;
import br.app.camarada.backend.enums.StatusPropaganda;
import br.app.camarada.backend.enums.TipoPublicacao;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReqPublicacaoDePropaganda {

    private String texto;
    private TipoPublicacao tipoPublicacao;
    private String resumo;
    private byte[] imagem;
    private Integer visualizacoes;
    private Integer NaBibliotecaDePessoas;
    private Integer curtidas;
    private String manchete;
    private Long idPerfil;
    private CategoriaPublicacao categoriaPublicacao;
    private String categoriasDaPropaganda;
    private Boolean emPropaganda;

    private StatusPropaganda statusPropaganda;
}
