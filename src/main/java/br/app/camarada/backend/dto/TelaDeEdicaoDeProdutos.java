package br.app.camarada.backend.dto;


import br.app.camarada.backend.entidades.Produto;
import br.app.camarada.backend.enums.CategoriaProduto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelaDeEdicaoDeProdutos {
    private Long id;
    private String nome;
    private BigDecimal preco;
    private Double avaliacao;
    private byte[] image;
    private String descricao;
    private CategoriaDto categoriaDoProduto;
    private List<CategoriaDto> categorias;
    private Boolean disponivel;

    public static TelaDeEdicaoDeProdutos build(Produto produto, List<CategoriaDto> categorias) {
        CategoriaProduto categoriaProduto = null;
        if(produto.getCategoriaProduto() == null){
            categoriaProduto= CategoriaProduto.OUTRO;
        }else {
            categoriaProduto= produto.getCategoriaProduto();
        }
        return new TelaDeEdicaoDeProdutos(
                produto.getId(),
                produto.getNome(),
                produto.getPreco(),
                produto.getAvaliacao(),
                produto.getImagem(),
                produto.getDescricao(),
                new CategoriaDto(categoriaProduto.getNome(), categoriaProduto.name()),
                categorias,
                produto.getDisponivel()
        );
    }
}
