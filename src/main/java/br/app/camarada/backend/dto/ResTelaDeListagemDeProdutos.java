package br.app.camarada.backend.dto;

import br.app.camarada.backend.enums.Cores;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResTelaDeListagemDeProdutos {
    private List<ProdutoParaEstabelecimento> lista;
    private List<CategoriaDto> categorias;
    private List<CoresDto> cores;
}
