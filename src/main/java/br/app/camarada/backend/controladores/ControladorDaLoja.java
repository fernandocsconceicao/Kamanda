package br.app.camarada.backend.controladores;

import br.app.camarada.backend.dto.*;
import br.app.camarada.backend.dto.publicacao.req.RequisicaoDePostagem;
import br.app.camarada.backend.dto.publicacao.res.RespostaPublicacoes;
import br.app.camarada.backend.enums.Cabecalhos;
import br.app.camarada.backend.exception.NomeDeUsuarioExistente;
import br.app.camarada.backend.filtros.CustomServletWrapper;
import br.app.camarada.backend.servicos.ServicoDaLoja;
import br.app.camarada.backend.servicos.ServicoDePagamentos;
import br.app.camarada.backend.servicos.ServicoParaFeed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.web.bind.annotation.*;

import javax.persistence.GeneratedValue;

@RestController
@RequestMapping("/loja")

@AllArgsConstructor
@Slf4j
public class ControladorDaLoja {
    private ServicoParaPerfil servicoParaPerfil;
    private ServicoParaFeed servicoParaFeed;
    private ServicoDaLoja servicoDaLoja;

    @GetMapping(value ="vitrine" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TelaVitrine> obterVitrine( CustomServletWrapper request)  {
        long l = Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue()));
        TelaVitrine telaVitrine =
                servicoDaLoja.obterVitrine(
                        DadosDeCabecalhos.builder().idUsuario(l).build()
                );

        return ResponseEntity.ok().body(telaVitrine);
    }


    @PostMapping("carrinho")
    public ResponseEntity<TelaCarrinho> getCartScreen(CustomServletWrapper request) throws JsonProcessingException {
        log.info("Começando request de tela de Carrinho");
        TelaCarrinho screen = servicoDaLoja.obterCarrinho(DadosDeCabecalhos.builder().idUsuario(
                        Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue()))
                ).build()
        );
        log.info("Finalizando request de tela de Carrinho");
        return ResponseEntity.ok().body(screen);
    }
    @PostMapping("adicionarprodutoaocarrinho")
    public ResponseEntity<Void> addProductToCart(@RequestBody AdicionamentoDeProdutoAoCarrinho dto, CustomServletWrapper request) throws JsonProcessingException {
        servicoDaLoja.adicionarAoCarrinho(
                dto,DadosDeCabecalhos.builder().idUsuario(
                        Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue()))
                ).build());

        return ResponseEntity.ok().build();
    }
    @PostMapping("removerprodutoaocarrinho")
    public ResponseEntity<Void> removerDoCarrinho(@RequestBody AdicionamentoDeProdutoAoCarrinho dto, CustomServletWrapper request) throws JsonProcessingException {
        servicoDaLoja.retirarProdutoDoCarrinho(
                dto,DadosDeCabecalhos.builder().idUsuario(
                        Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue()))
                ).build());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/produto/adicionar")
    public ResponseEntity<Void> adicionarProduto(){
        return ResponseEntity.ok().build();
    }
    @PostMapping("/produto/remover")
    public ResponseEntity<Void> removerProduto(){
        return ResponseEntity.ok().build();
    }

}
