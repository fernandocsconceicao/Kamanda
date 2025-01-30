package br.app.camarada.backend.entidades;

import br.app.camarada.backend.enums.StatusPropaganda;
import br.app.camarada.backend.enums.TipoPropaganda;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Propaganda {
    @Id
    @GeneratedValue()
    private Long id;
    @ManyToMany
    private List<Publicacao> publicacoes;
    private Usuario autor;
    private Integer exibicoes;
    private String categorias;
    @Enumerated
    private StatusPropaganda statusPropaganda;
    private TipoPropaganda tipoPropaganda;
    private Integer maxDeExibicoes;

}
