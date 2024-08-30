package br.app.camarada.backend.entidades;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permissao implements GrantedAuthority {
    @Id
    @GeneratedValue
    private Long id;
    private String nome;

    @Override
    public String getAuthority() {
        return getNome();
    }
}
