package br.app.camarada.backend.dto;

import lombok.Data;

@Data
public class AdicionamentoDeProdutoAoCarrinho {
    private Long idProduto;
    private Integer quantidade;
}
