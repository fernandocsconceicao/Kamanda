package br.app.camarada.backend.entidades;

import br.app.camarada.backend.dto.ConteudoDaPublicacao;
import br.app.camarada.backend.enums.CategoriaPublicacao;
import br.app.camarada.backend.enums.TipoPublicacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Publicacao implements IPublicacao {
    @Id
    @GeneratedValue
    private Long id;
    @Lob
    private String texto;
    @Enumerated
    private TipoPublicacao tipoPublicacao;
    @ManyToOne
    private Perfil autorPrincipal;
    private LocalDateTime data;
    @Lob
    private String resumo;
    @Lob
    private byte[] imagem;
    private Integer visualizacoes;
    private Integer NaBibliotecaDePessoas;
    private Integer curtidas;
    private String manchete;
    private Long idPerfil;
    @Enumerated
    private CategoriaPublicacao categoriaPublicacao;
    private Boolean emPropaganda;

    public static Publicacao montar(String texto,
                                    TipoPublicacao tipo,
                                    Perfil autorPrincipal,
                                    LocalDateTime data,
                                    String resumo,
                                    byte[] imagem,
                                    String manchete,
                                    Long idPerfil,
                                    CategoriaPublicacao categoriaPublicacao) {
        return new Publicacao(null, texto, tipo, autorPrincipal, data,
                resumo, imagem, 0, 0,
                0, manchete, idPerfil,categoriaPublicacao , false);
    }


    @Override
    public ConteudoDaPublicacao getConteudo() {
        return new ConteudoDaPublicacao(
                this.id,
                this.texto,
                this.tipoPublicacao,
                this.autorPrincipal,
                this.data,
                this.resumo,
                this.imagem,
                this.visualizacoes,
                this.NaBibliotecaDePessoas,
                this.curtidas,
                this.manchete,
                this.idPerfil,
                this.categoriaPublicacao,
                this.emPropaganda);
    }

}
