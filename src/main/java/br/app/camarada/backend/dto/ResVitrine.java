package br.app.camarada.backend.dto;

import br.app.camarada.backend.entidades.Produto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class ResVitrine {
    private List<Produto> produtos;
}
