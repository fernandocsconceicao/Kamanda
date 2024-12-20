package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ItemCarrinhoComImagem {
    private Long id;
    private String titulo;
    private Integer quantidade;
    private BigDecimal preco;
    private byte[] imagem;

    /*(
       val id: Long,
    val image: String,
    val title: String,
    var amount: Int
     */
}
