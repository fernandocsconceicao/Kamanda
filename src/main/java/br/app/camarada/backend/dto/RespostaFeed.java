package br.app.camarada.backend.dto;

import br.app.camarada.backend.entidades.Publicacao;
import br.app.camarada.backend.enums.TipoErro;
import br.app.camarada.backend.enums.TipoServico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespostaFeed {
    private List<PublicacaoDto> publicacoes;
    private Boolean pagamentoPendente;
    private String codigo;
    private String erroTitulo;
    private String erroDescricao;
    private TipoErro tipoErro;
    private Boolean primeiroAcesso;
    @Nullable
    private TipoServico tipoServico;
}
