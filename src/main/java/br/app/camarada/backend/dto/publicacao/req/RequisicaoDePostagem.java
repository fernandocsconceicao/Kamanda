package br.app.camarada.backend.dto.publicacao.req;

import br.app.camarada.backend.enums.TipoPostagem;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
public class RequisicaoDePostagem {
    private String texto;
    private TipoPostagem tipoPostagem;
    @Nullable
    private List<Long> idsDePerfisMencionados;
    private String resumo;

}
