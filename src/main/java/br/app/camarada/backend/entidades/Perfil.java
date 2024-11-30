package br.app.camarada.backend.entidades;

import br.app.camarada.backend.enums.RotulosPerfil;
import br.app.camarada.backend.enums.TipoPerfil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ElementCollection(targetClass = RotulosPerfil.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "perfil_rotulos", joinColumns = @JoinColumn(name = "perfil_id"))
    @Column(name = "rotulo")
    private Set<RotulosPerfil> rotulosEscolhidos = new HashSet<>();
    private Integer idade;
    private String resumo;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] imagem;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] imagemFundo;

    private String minhaBibliotecaJson;

}
