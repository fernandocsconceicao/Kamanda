package br.app.camarada.backend.dto;

import br.app.camarada.backend.enums.CategoriaPublicacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequisicaoFeed {
    private CategoriaPublicacao categoria;
}
