package br.app.camarada.backend.dto.postagem.req;

import br.app.camarada.backend.enums.TipoPostagem;
import lombok.Data;

import java.util.List;

@Data
public class RequisicaoDePostagem {
    private String texto;
    private TipoPostagem tipoPostagem;
    private List<Long> idsDePerfisMencionados;

}
