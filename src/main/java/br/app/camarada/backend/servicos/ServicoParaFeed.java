package br.app.camarada.backend.servicos;

import br.app.camarada.backend.entidades.Publicacao;
import br.app.camarada.backend.repositorios.RepositorioDePublicacoes;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ServicoParaFeed {
    private RepositorioDePublicacoes repositorioDePublicacoes;

    public List<Publicacao> buscarPublicacoes (){
        return repositorioDePublicacoes.findAll();
    }
}
