package br.app.camarada.backend.controladores;


import br.app.camarada.backend.dto.publicacao.res.RespostaPublicacoes;
import br.app.camarada.backend.entidades.Publicacao;
import br.app.camarada.backend.servicos.ServicoParaFeed;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("feed")
@AllArgsConstructor
public class ControladorFeed {
    private ServicoParaFeed servicoParaFeed;
    @GetMapping("obter")
    public ResponseEntity<RespostaPublicacoes> buscarFeed(){
        List<Publicacao> publicacoes = servicoParaFeed.buscarPublicacoes();
        return ResponseEntity.ok().body(RespostaPublicacoes.montar(publicacoes));
    }
}
