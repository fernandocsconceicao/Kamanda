package br.app.camarada.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqPrimeiroAcessoEstabelecimento {

    private Long idDeEstabelecimento;

    private String logo;
    private BigDecimal valorMinimoDePedido;
    private String descricao;
    private Date inicioExpediente;
    private Date fimExpediente;
    private Boolean pedidosInstantaneos;
    private Long idRegiao;
    @JsonProperty("endereco")
    private String rua;
    private String telefone;
    private String cep;
    private BigDecimal valorMinimo;
    private String complemento;
    private String numero;
    private String cidade;
    private String estado;


}
