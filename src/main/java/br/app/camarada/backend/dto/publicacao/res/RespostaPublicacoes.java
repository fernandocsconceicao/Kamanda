package br.app.camarada.backend.dto.publicacao.res;

import br.app.camarada.backend.dto.PerfilDto;
import br.app.camarada.backend.dto.PublicacaoDto;
import br.app.camarada.backend.entidades.Publicacao;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class RespostaPublicacoes {
    private List<PublicacaoDto> publicacoes;

    public static RespostaPublicacoes montarPublicacaoReduzida(List<Publicacao> publicacoes) {
        List<PublicacaoDto> retorno = new ArrayList<>();
        publicacoes.forEach(p -> retorno.add(new PublicacaoDto(p.getId(),
                                p.obterTipoDeConteudo(),
                                PerfilDto.montar(p.obterAutorPrincipal()),
                                p.getResumo(),
                                "28-11-2024",
                                null
                        )
                )
        );

        return new RespostaPublicacoes(retorno);
    }
}
