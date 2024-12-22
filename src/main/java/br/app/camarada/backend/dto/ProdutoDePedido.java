package br.app.camarada.backend.dto;

import br.app.camarada.backend.entidades.Produto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ProdutoDePedido {
    private String nome;
    private Integer quantidade;
    private Long idProduto;
    private byte[] miniatura;

    public static ProdutoDePedido build(ItemCarrinho produtoCarrinho, Produto produto) {
        String nomeProduto = produto.getNome();
        Integer quantidade = produtoCarrinho.getQuantidade();
        Long id = produtoCarrinho.getId();
        return new ProdutoDePedido(nomeProduto, quantidade, id, produto.getImagem());
    }
}
