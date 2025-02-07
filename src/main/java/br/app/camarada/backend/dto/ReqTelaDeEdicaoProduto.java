package br.app.camarada.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReqTelaDeEdicaoProduto {
    private String idDoEstabelecimento;
    private String idDoProduto;
}
