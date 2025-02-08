package br.app.camarada.backend.entidades;

import br.app.camarada.backend.dto.ReqCriacaoDeProduto;
import br.app.camarada.backend.enums.CategoriaProduto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Produto {
    @Id
    @GeneratedValue
    private Long id;
    private String nome;

    private Long estabelecimentoId;
    @Lob
    private byte[] imagem;
    private LocalDate dataDeCriacao;
    @Enumerated
    private CategoriaProduto categoriaProduto;
    private BigDecimal preco;
    private BigDecimal precoVitrine;
    private Double avaliacao;
    private LocalDateTime dataExibicao;
    private Long idRegiao;
    private String marca;
    private String modelo;
    private String descricao;
    private String tamanho;
    private String material;
    private Integer estoque;
    private Integer prazoDeEntrega;
    private Integer popularidade;
    private Boolean disponivel;
    @Lob
    private byte[] imagem2;
    @Lob
    private byte[] imagemDeExibicaoDeRoupa;

    public static Produto build(ReqCriacaoDeProduto dto, Estabelecimento estabelecimento) {
        return new Produto(
                null,
                dto.getNome(),
                estabelecimento.getId(),
                dto.getImagem(),
                LocalDate.now(),
                dto.getCategoriaProduto(),
                dto.getPreco(),
                dto.getPreco().multiply(BigDecimal.valueOf(1.20)),
                5.0d,
                LocalDateTime.now(),
                null,
                dto.getMarca(),
                dto.getModelo(),
                dto.getDescricao(),
                dto.getTamanho(),
                dto.getMaterial(),
                dto.getEstoque(),
                dto.getPrazoDeEntrega(),
                0,
                dto.getDisponivel(),
                null,
                null
        );

    }

}
