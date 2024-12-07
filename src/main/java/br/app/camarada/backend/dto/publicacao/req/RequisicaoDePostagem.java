package br.app.camarada.backend.dto.publicacao.req;

import br.app.camarada.backend.enums.TipoPublicacao;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
public class RequisicaoDePostagem {
    private String texto;
    private TipoPublicacao tipoPublicacao;
    @Nullable
    private List<Long> idsDePerfisMencionados;
    private String resumo;
    private byte[] imagem;

}
