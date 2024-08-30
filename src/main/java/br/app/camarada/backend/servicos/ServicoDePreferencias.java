package br.app.camarada.backend.servicos;


import br.app.camarada.backend.entidades.Preferencias;
import br.app.camarada.backend.repositorios.PreferenciasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServicoDePreferencias {
    private final PreferenciasRepository preferenciasRepository;


    Preferencias obterPorId(Long id){
        return preferenciasRepository.findById(id).get();
    }
    Preferencias salvar(Preferencias entidade){
        return preferenciasRepository.save(entidade);
    }
}
