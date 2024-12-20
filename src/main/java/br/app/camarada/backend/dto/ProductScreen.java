package br.app.camarada.backend.dto;

import br.app.camarada.backend.entidades.Produto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Data
public class ProductScreen {
    private Long id;
    private String title;
    private String description;
    private byte[] image;
    private String propertiesTitle;
    private List<Properties> properties;
    private BigDecimal preco;

    public static ProductScreen build(Produto produto, List<Properties> properties, BigDecimal preco) {
        return new ProductScreen(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getImagem(),
                "Sobre o produto",
                properties,
                preco);
    }

}
