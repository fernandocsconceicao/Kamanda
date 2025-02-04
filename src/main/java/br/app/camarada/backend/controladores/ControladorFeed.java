package br.app.camarada.backend.controladores;


import br.app.camarada.backend.dto.*;
import br.app.camarada.backend.dto.publicacao.req.RequisicaoParaObterPublicacao;
import br.app.camarada.backend.dto.publicacao.res.RespostaPublicacoes;
import br.app.camarada.backend.entidades.Publicacao;
import br.app.camarada.backend.enums.Cabecalhos;
import br.app.camarada.backend.filtros.CustomServletWrapper;
import br.app.camarada.backend.servicos.ServicoDePagamentos;
import br.app.camarada.backend.servicos.ServicoParaFeed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("feed")
@AllArgsConstructor
public class ControladorFeed {
    private ServicoParaFeed servicoParaFeed;
    private ServicoDePagamentos servicoDePagamentos;

    @PostMapping("/propaganda/publicar")
    public ResponseEntity<RespostaPublicacoes> buscarFeed(@RequestBody ReqPublicacaoDePropaganda dto, CustomServletWrapper request){
        DadosDeCabecalhos dadosDeCabecalhos =  DadosDeCabecalhos.builder()
                .idPerfilPrincipal(Long.parseLong(request.getHeader(Cabecalhos.PERFIL.getValue()).toString()))
                .email(request.getHeader(Cabecalhos.EMAIL.getValue()))
                .idUsuario(Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue())))
                .build();
        servicoParaFeed.publicarPropaganda(dto,dadosDeCabecalhos);
        return ResponseEntity.ok().build();
    }

    @PostMapping("obter")
    public ResponseEntity<RespostaPublicacoes> buscarFeed(@RequestBody RequisicaoFeed dto, CustomServletWrapper request) {
        DadosDeCabecalhos dadosDeCabecalhos =  DadosDeCabecalhos.builder()
                .idPerfilPrincipal(Long.parseLong(request.getHeader(Cabecalhos.PERFIL.getValue()).toString()))
                .email(request.getHeader(Cabecalhos.EMAIL.getValue()))
                .idUsuario(Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue())))
                .build();
        RespostaFeed respostaFeed = servicoParaFeed.buscarPublicacoes(dadosDeCabecalhos,dto);
        String contafinanceira = request.getHeader(Cabecalhos.CONTA_FINANCEIRA.getValue());
        String usuarioId = request.getHeader(Cabecalhos.USUARIO.getValue());
        try{
            Long.parseLong(contafinanceira);
        }catch (NumberFormatException e){
            servicoDePagamentos.criarContaFinanceira(Long.valueOf(usuarioId));
            System.out.println(" conta financeira criada");
        }
        RespostaPublicacoes resposta = RespostaPublicacoes.montarPublicacaoReduzida(
                respostaFeed.getPublicacoes(),
                respostaFeed.getPagamentoPendente(),
                respostaFeed.getCodigo(),
                respostaFeed.getErroTitulo(),
                respostaFeed.getErroDescricao(),
                respostaFeed.getTipoErro(),
                respostaFeed.getTipoServico(),
                respostaFeed.getPodePublicar()
        );
        System.out.println(resposta.getPublicacoes().size());

        return ResponseEntity.ok().body(resposta);
    }
    @PostMapping("obterpublicacao")
    public ResponseEntity<PublicacaoDto> obterPublicacao(@RequestBody RequisicaoParaObterPublicacao dto, CustomServletWrapper request){
       PublicacaoDto publicacao = servicoParaFeed.obterPublicacao(dto);
        return ResponseEntity.ok().body(publicacao);
    }

}
