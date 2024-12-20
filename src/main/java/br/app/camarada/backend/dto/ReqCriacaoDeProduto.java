package br.app.camarada.backend.dto;

import br.app.camarada.backend.enums.CategoriaProduto;
import br.app.camarada.backend.enums.Cores;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class ReqCriacaoDeProduto {

    private byte[] imagem;
    private String nome;
    private CategoriaProduto categoriaProduto;
    private BigDecimal preco;
    private String cor;
    private String marca;
    private String modelo;
    private String descricao;
    private Integer prazoDeEntrega;
    private Boolean disponivel;
    private Integer estoque;
    private String tamanho;
    private String material;

}
