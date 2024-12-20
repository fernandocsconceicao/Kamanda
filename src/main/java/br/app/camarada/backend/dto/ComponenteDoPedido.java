package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
@AllArgsConstructor
@Data
public class ComponenteDoPedido {
    private String nome;
    private String preco;

}
