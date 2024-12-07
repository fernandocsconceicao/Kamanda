package br.app.camarada.backend.entidades;

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

    public static Publicacao montar(String texto,
                                    TipoPublicacao tipo,
                                    Perfil autorPrincipal,
                                    LocalDateTime data,
                                    String resumo,
                                    byte[] imagem) {
        return new Publicacao(null, texto, tipo, autorPrincipal,data,resumo,imagem);
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
