package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CarrinhoDto {
    private List<ItemCarrinho> produtos;
    private String valorTotal;
}
