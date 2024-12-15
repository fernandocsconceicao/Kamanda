package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;

import java.util.List;
@AllArgsConstructor
public class TelaCarrinho {
    /**
     *     val description: String,
     *     val products: MutableList<CartProductDto>,
     *     val totalValue: String
     */
    private List<ItemCarrinho> produtos;
}
