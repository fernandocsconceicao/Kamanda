package br.app.camarada.backend.dto.postagem.req;

import br.app.camarada.backend.enums.TipoPostagem;
import lombok.Data;

@Data
public class RequisicaoDePostagem {
    private String texto;
    private TipoPostagem tipoPostagem;

}
