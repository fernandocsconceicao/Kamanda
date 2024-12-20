package br.app.camarada.backend.servicos;

import br.app.camarada.backend.dto.RegiaoDto;
import br.app.camarada.backend.entidades.Regiao;
import br.app.camarada.backend.repositorios.RepositorioDeRegiao;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ServicoDeRegiao {
    private RepositorioDeRegiao regiaoRepository;

    public Optional<Regiao> encontrarPorId(Long id) {
        return regiaoRepository.findById(id);
    }

    public List<RegiaoDto> listar() {
        List<Regiao> all = regiaoRepository.findAll();
        List<RegiaoDto> resposta = new ArrayList<>();
        for (Regiao regiao : all) {
            resposta.add(new RegiaoDto(regiao.getId(), regiao.getNome()));
        }
        return resposta;
    }
}
