package br.app.camarada.backend.dto.publicacao.res;

import br.app.camarada.backend.dto.PerfilPublicacaoDto;
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
    private Boolean podePublicar;


    public static RespostaPublicacoes montarPublicacaoReduzida(List<PublicacaoDto> publicacoes,
                                                               Boolean pagamentoPendente,
                                                               String codigo,
                                                               String erroTitulo,
                                                               String erroDescricao,
                                                               TipoErro tipoErro,
                                                               TipoServico tipoServico,
                                                               Boolean podePublicar
    ) {


        return new RespostaPublicacoes(publicacoes, pagamentoPendente, codigo, erroTitulo, erroDescricao, tipoErro, tipoServico,podePublicar);
    }
}
