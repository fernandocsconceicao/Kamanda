package br.app.camarada.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdicionamentoDeProdutoAoCarrinho {
    private List<ItemCarrinho> produtos;

}

