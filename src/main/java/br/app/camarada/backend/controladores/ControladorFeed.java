package br.app.camarada.backend.controladores;


import br.app.camarada.backend.dto.PublicacaoDto;
import br.app.camarada.backend.dto.publicacao.req.RequisicaoParaObterPublicacao;
import br.app.camarada.backend.dto.publicacao.res.RespostaPublicacoes;
import br.app.camarada.backend.entidades.Publicacao;
import br.app.camarada.backend.filtros.CustomServletWrapper;
import br.app.camarada.backend.servicos.ServicoParaFeed;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("feed")
@AllArgsConstructor
public class ControladorFeed {
    private ServicoParaFeed servicoParaFeed;
    @GetMapping("obter")
    public ResponseEntity<RespostaPublicacoes> buscarFeed(){
        List<Publicacao> publicacoes = servicoParaFeed.buscarPublicacoes();
        return ResponseEntity.ok().body(RespostaPublicacoes.montarPublicacaoReduzida(publicacoes));
    }
    @PostMapping("obterpublicacao")
    public ResponseEntity<PublicacaoDto> buscarFeed(@RequestBody RequisicaoParaObterPublicacao dto, CustomServletWrapper request){
        System.out.println("fsd");
       PublicacaoDto publicacao = servicoParaFeed.obterPublicacao(dto);
        return ResponseEntity.ok().body(publicacao);
    }
}
