package br.app.camarada.backend.dto;

import br.app.camarada.backend.enums.CategoriaProduto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqEdicaoProduto {
    private String idDoProduto;
    private String nome;
    private BigDecimal preco;
    private byte[] imagem;
    private Boolean trocouImagem;
    private String descricao;
    private Boolean disponivel;
    private CategoriaProduto categoria;

}
