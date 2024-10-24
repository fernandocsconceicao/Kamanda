package br.app.camarada.backend.servicos;


import br.app.camarada.backend.entidades.Estatisticas;
import br.app.camarada.backend.repositorios.RepositorioEstatisticas;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicoDeEstatisticas {
    private final RepositorioEstatisticas repositorioEstatisticas;

    public void tabularPostagem(){
        Optional<Estatisticas> byId = repositorioEstatisticas.findById(1L);
        if(byId.isPresent()){

        }

    }
}
