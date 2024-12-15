package br.app.camarada.backend.dto;

import br.app.camarada.backend.entidades.Produto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProdutoParaEstabelecimento {

    private Long id;
    private byte[] imagem;
    private String titulo;
    private BigDecimal preco;

    public static List<ProdutoParaEstabelecimento> build(List<Produto> produtos) {
        ArrayList<ProdutoParaEstabelecimento> lista = new ArrayList<>();
        produtos.forEach(i -> lista.add(new ProdutoParaEstabelecimento(i.getId(),
                                i.getImagem(),
                                i.getNome(),
                                i.getPreco()
                        )
                )
        );
        return lista;
    }
}
