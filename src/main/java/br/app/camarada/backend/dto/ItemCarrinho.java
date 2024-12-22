package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ItemCarrinho {
    private Long id;
    private String titulo;
    private Integer quantidade;
    private BigDecimal preco;
    private Long idEstabelecimento;

}
