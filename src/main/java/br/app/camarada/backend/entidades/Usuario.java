package br.app.camarada.backend.entidades;

import br.app.camarada.backend.enums.TipoConta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuario")
public class Usuario implements UserDetails {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String senha;
    @Enumerated
    private TipoConta tipoConta;
    @Column(unique = true)
    private String email;
    private String nome;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Permissao> permissoes;
    private Boolean primeiroAcesso;
    private String codigoConfirmacao;
    private Boolean emailConfirmado;
    private String codigoEsqueciaSenha;
    private Boolean prontoParaMudarDeSenha;
    private String senhaAntiga;
    private Boolean pingpongEstaOk;
    @OneToOne(fetch = FetchType.LAZY)
    private ContaFinanceira contaFinanceira;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Perfil> perfil;
    private Long perfilPrincipalId;
    private String carrinho;
    private String idDaSessaoWs;
    @ManyToOne
    private Regiao regiao;
    private BigDecimal simulacaoPrecoFinalDaSimulacao;
    private Double simulacaoDistancia;
    private BigDecimal simulacaoFrete;
    private Long estabelecimentoId;

    private Long enderecoId;
    private Boolean primeiraCompra;
    private Boolean emCompra;
    private String cpf;
    private String telefone;
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
