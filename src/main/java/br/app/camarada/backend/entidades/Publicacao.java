package br.app.camarada.backend.entidades;

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
public class Publicacao implements Conteudo {
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
    private Integer NaBibliotecaDePessoas;
    private Integer curtidas;
    private String manchete;
    private Long idPerfil;
    @Enumerated
    private CategoriaPublicacao categoria;

    public static Publicacao montar(String texto,
                                    TipoPublicacao tipo,
                                    Perfil autorPrincipal,
                                    LocalDateTime data,
                                    String resumo,
                                    byte[] imagem,
                                    String manchete,
                                    Long idPerfil,
                                    CategoriaPublicacao categoriaPublicacao) {
        return new Publicacao(null, texto, tipo, autorPrincipal,data,resumo,imagem, 0,0,0,manchete,idPerfil,categoriaPublicacao);
    }

    @Override
    public String obtertextoDaPostagem() {
        return this.texto;
    }

    @Override
    public TipoPublicacao obterTipoDeConteudo() {
        return this.tipoPublicacao;
    }

    @Override
    public Perfil obterAutorPrincipal() {
        return this.autorPrincipal;
    }
}
