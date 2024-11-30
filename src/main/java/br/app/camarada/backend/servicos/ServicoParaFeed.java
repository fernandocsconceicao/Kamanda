package br.app.camarada.backend.servicos;

import br.app.camarada.backend.dto.PerfilDto;
import br.app.camarada.backend.dto.PublicacaoDto;
import br.app.camarada.backend.dto.publicacao.req.RequisicaoParaObterPublicacao;
import br.app.camarada.backend.entidades.Publicacao;
import br.app.camarada.backend.repositorios.RepositorioDePublicacoes;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ServicoParaFeed {
    private RepositorioDePublicacoes repositorioDePublicacoes;

    public List<Publicacao> buscarPublicacoes() {
        return repositorioDePublicacoes.findAll();
    }

    public PublicacaoDto obterPublicacao(RequisicaoParaObterPublicacao dto) {
        System.out.println(dto.getIdPublicacao());
        Publicacao publicacao = repositorioDePublicacoes.findById(dto.getIdPublicacao()).get();
        System.out.println("fdsfsd");
        String data;
        if (publicacao.getData() == null) {
            data = LocalDateTime.now().toString();
        }else{
            data = publicacao.getData().toString();
        }
        return new PublicacaoDto(publicacao.getId(),
                publicacao.getTipoPostagem(),
                PerfilDto.montar(publicacao.getAutorPrincipal()),
                publicacao.getResumo(),
                data.toString(),
                publicacao.getTexto()
        );

    }
}
