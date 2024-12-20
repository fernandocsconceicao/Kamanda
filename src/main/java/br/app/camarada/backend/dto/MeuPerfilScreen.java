package br.app.camarada.backend.dto;

import br.app.camarada.backend.entidades.Endereco;
import br.app.camarada.backend.entidades.Estabelecimento_Endereco;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class MeuPerfilScreen {

    private String nome;
    private byte[] image;
    private String descricao;
    private BigDecimal valorMinimo;
    private String endereco;
    private String telefone;
    private LocalTime inicioExpediente;
    private LocalTime fimExpediente;
    private String cep;
    private Boolean abertoFechado;
    private String cidade;
    private String estado;
    private String complemento;
    private String numero;
    public static MeuPerfilScreen build(Estabelecimento_Endereco estabelecimento) {
        String cidade="";
        String estado ="";
        if( estabelecimento.getEndereco() != null){
            Endereco e = estabelecimento.getEndereco();
            cidade = e.getCidade();
            estado = e.getEstado();
        }
        return new MeuPerfilScreen(estabelecimento.getName(),
                estabelecimento.getLogo(),
                estabelecimento.getDescription(),
                estabelecimento.getMinOrder(),
                estabelecimento.getEnderecoCompleto(),
                estabelecimento.getTelefone(),
                estabelecimento.getInicioExpediente(),
                estabelecimento.getFimExpediente(),
                estabelecimento.getEndereco().getCep(),
                estabelecimento.getOpenClosed(),
                cidade,
                estado,
                estabelecimento.getEndereco().getComplemento(),
                estabelecimento.getEndereco().getNumero()
        );
    }
}
