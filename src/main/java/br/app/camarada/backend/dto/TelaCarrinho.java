package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TelaCarrinho {
    /**
     *     val description: String,
     *     val products: MutableList<CartProductDto>,
     *     val totalValue: String
     */
    private List<ItemCarrinhoComImagem> produtos;
    private String preçoTotal;
    private Boolean primeiraCompra;
    private Boolean emailCadastrado;
}
