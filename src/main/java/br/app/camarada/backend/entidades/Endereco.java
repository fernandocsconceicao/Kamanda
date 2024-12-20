package br.app.camarada.backend.entidades;

import br.app.camarada.backend.enums.TipoDeLocalidade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String endereco;
    private String numero;
    private String complemento;
    @OneToOne(fetch = FetchType.LAZY)
    private Usuario usuario;

    private Boolean favorito;
    private String rotulo;
    private String cep;
    private String googlePlaceId;
    private String enderecoCompleto;
    private TipoDeLocalidade tipoDeLocalidade;
    private String cidade;
    private String estado;
    @OneToOne
    private Estabelecimento_Endereco estabelecimento;
    @OneToOne
    private Estabelecimento estabelecimento_produtos;
    public static Endereco build(Usuario usuario){
        return new Endereco(
                null,null,null,null,
                usuario,false,null,
                null,null,null,null,null,
                null,null,null);
    }
}
