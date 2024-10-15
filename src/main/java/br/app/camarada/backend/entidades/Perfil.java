package br.app.camarada.backend.entidades;

import br.app.camarada.backend.enums.TipoPerfil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Perfil {
    @GeneratedValue
    @Id
    private Long id;

    @ManyToMany
    private List<Publicacao> publicacao;
    @Enumerated
    private TipoPerfil tipoPerfil;
    private Boolean ativo;
    private TipoPerfil tipo;
    private String nome;
    private Boolean verificado;
    @ManyToOne
    private Usuario usuario;
    @Column(unique = true)
    private String nomeUsuario;
    private String telefone;
}
