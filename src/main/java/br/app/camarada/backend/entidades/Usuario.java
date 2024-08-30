package br.app.camarada.backend.entidades;

import br.app.camarada.backend.enums.TipoConta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Usuario implements UserDetails {
    @GeneratedValue
    @Id
    private Long id;
    private String senha;
    @Enumerated
    private TipoConta tipoConta;
    @Column(unique = true)
    private String email;
    private Long totemId;
    private String nome;
    @OneToMany(fetch= FetchType.EAGER)
    private List<Permissao> permissoes;
    private Long establishmentId;
    private Boolean primeiroAcesso;
    private Long carrinhoId;
    private Long entregadorId;

    private Long clienteId; // TODO: Atrelar A entidade cliente a um Usuario do tipo Cliente

    private String codigoConfirmacao;
    private Boolean emailConfirmado;
    private String codigoEsqueciaSenha;
    private Boolean prontoParaMudarDeSenha;
    private String senhaAntiga;

    private Long idPreferencia;
    private String idDaSessaoWs;
    private Boolean pingpongEstaOk;
    @OneToOne(fetch = FetchType.EAGER)
    private ContaFinanceira contaFinanceira;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getPermissoes();
    }

    @Override
    public String getPassword() {
        return getSenha();
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
