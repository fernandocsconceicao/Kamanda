package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostagemDeEdicaoDeEstabelecimento {
    private Long idDeEstabelecimento;
    private String nome;
    private byte[] image;
    private String descricao;

    private String endereco;
    private String telefone;
    private String cep;
    private BigDecimal valorMinimo;
    private String complemento;
    private String numero;
    private String cidade;
    private String estado;
}
