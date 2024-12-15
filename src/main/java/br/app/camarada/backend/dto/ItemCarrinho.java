package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ItemCarrinho {
    private Long id;
    private String titulo;
    private Integer quantidade;
    /*(
       val id: Long,
    val image: String,
    val title: String,
    var amount: Int
     */
}
