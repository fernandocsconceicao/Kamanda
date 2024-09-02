package br.app.camarada.backend.entidades;

import br.app.camarada.backend.enums.TipoPostagem;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Publicacao implements Conteudo {
    @Id
    @GeneratedValue
    private Long id;
    private String texto;
    @Enumerated
    private TipoPostagem tipoPostagem;
    @ManyToMany
    private List<Perfil> perfil;


    public static Publicacao montar(String texto,
                                    TipoPostagem tipo,
                                    List<Perfil> perfil) {

        return new Publicacao(null, texto, tipo, perfil);
    }

    @Override
    public String obtertextoDaPostagem() {
        return this.texto;
    }
}
