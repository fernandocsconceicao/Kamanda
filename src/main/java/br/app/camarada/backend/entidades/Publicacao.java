package br.app.camarada.backend.entidades;

import br.app.camarada.backend.enums.TipoPostagem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Publicacao implements Conteudo {
    @Id
    @GeneratedValue
    private Long id;
    private String texto;
    @Enumerated
    private TipoPostagem tipoPostagem;
    @ManyToMany
    private List<Perfil> perfisMencionados;
    @ManyToOne
    private Perfil autorPrincipal;
    private LocalDateTime localDateTime;


    public static Publicacao montar(String texto,
                                    TipoPostagem tipo,
                                    List<Perfil> perfisMencionados,
                                    Perfil autorPrincipal,
                                    LocalDateTime data) {


        return new Publicacao(null, texto, tipo, perfisMencionados, autorPrincipal,data);
    }

    @Override
    public String obtertextoDaPostagem() {
        return this.texto;
    }

    @Override
    public TipoPostagem obterTipoDeConteudo() {
        return this.tipoPostagem;
    }

    @Override
    public Perfil obterAutorPrincipal() {
        return this.autorPrincipal;
    }
}
