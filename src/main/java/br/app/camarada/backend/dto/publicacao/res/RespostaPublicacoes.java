package br.app.camarada.backend.dto.publicacao.res;

import br.app.camarada.backend.dto.PerfilDto;
import br.app.camarada.backend.dto.PublicacaoDto;
import br.app.camarada.backend.entidades.Publicacao;
import br.app.camarada.backend.enums.TipoErro;
import br.app.camarada.backend.enums.TipoServico;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class RespostaPublicacoes {
    private List<PublicacaoDto> publicacoes;
    private Boolean pagamentoPendente;
    private String codigo;
    private String erroTitulo;
    private String erroDescricao;
    private TipoErro tipoErro;
    private TipoServico tipoServico;

    public static RespostaPublicacoes montarPublicacaoReduzida(List<Publicacao> publicacoes,
                                                               Boolean pagamentoPendente,
                                                               String codigo,
                                                               String erroTitulo,
                                                               String erroDescricao,
                                                               TipoErro tipoErro,
                                                               TipoServico tipoServico
    ) {
        List<PublicacaoDto> retorno = new ArrayList<>();
        publicacoes.forEach(p -> retorno.add(new PublicacaoDto(p.getId(),
                                p.obterTipoDeConteudo(),
                                PerfilDto.montar(p.obterAutorPrincipal()),
                                p.getResumo(),
                                "28-11-2024",
                                null,
                                p.getImagem(),
                                p.getManchete(),
                                p.getAutorPrincipal().getImagem()
                        )
                )
        );

        return new RespostaPublicacoes(retorno, pagamentoPendente, codigo, erroTitulo, erroDescricao, tipoErro, tipoServico);
    }
}
