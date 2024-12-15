package br.app.camarada.backend.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Regiao {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany
    private List<Usuario> usuarios;
    @Column(unique = true)
    private String nome;
    @OneToMany
    private List<Estabelecimento> estabelecimentos;
}
