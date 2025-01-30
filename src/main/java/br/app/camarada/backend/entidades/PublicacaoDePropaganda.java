package br.app.camarada.backend.entidades;

import br.app.camarada.backend.dto.ConteudoDaPublicacao;
import br.app.camarada.backend.enums.CategoriaPublicacao;
import br.app.camarada.backend.enums.StatusPropaganda;
import br.app.camarada.backend.enums.TipoPublicacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PublicacaoDePropaganda implements IPublicacao {
    @Id
    @GeneratedValue
    private Long id;
    @Column(columnDefinition = "VARCHAR(15000) CHARACTER SET latin1 COLLATE latin1_swedish_ci")
    private String texto;
    @Enumerated
    private TipoPublicacao tipoPublicacao;
    @ManyToOne
    private Perfil autorPrincipal;
    private LocalDateTime data;
    @Column(columnDefinition = "VARCHAR(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci")
    private String resumo;
    @Lob
    private byte[] imagem;
    private Integer visualizacoes;
    private Integer naBibliotecaDePessoas;
    private Integer curtidas;
    private String manchete;
    private Long idPerfil;
    private String categoriasDaPropaganda;
    private Boolean propaganda;
    @Enumerated
    private StatusPropaganda statusPropaganda;
    @Enumerated
    private CategoriaPublicacao categoriaPublicacao;

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
                this.naBibliotecaDePessoas,
                this.curtidas,
                this.manchete,
                this.idPerfil,
                this.getCategoriaPublicacao(),
                true
        );
    }
}
