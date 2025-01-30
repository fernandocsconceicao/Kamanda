package br.app.camarada.backend.servicos;

import br.app.camarada.backend.entidades.Aplicacao;
import br.app.camarada.backend.entidades.Publicacao;
import br.app.camarada.backend.repositorios.RepositorioAplicacao;
import br.app.camarada.backend.repositorios.RepositorioDePublicacoes;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class ServicoParaAplicacao {
    private RepositorioDePublicacoes repositorioDePublicacoes;
    private RepositorioAplicacao repositorioAplicacao;

}
