package br.app.camarada.backend.controladores;

import br.app.camarada.backend.dto.*;
import br.app.camarada.backend.dto.publicacao.req.RequisicaoDePostagem;
import br.app.camarada.backend.dto.publicacao.res.RespostaPublicacoes;
import br.app.camarada.backend.enums.Cabecalhos;
import br.app.camarada.backend.exception.NomeDeUsuarioExistente;
import br.app.camarada.backend.filtros.CustomServletWrapper;
import br.app.camarada.backend.servicos.ServicoDePagamentos;
import br.app.camarada.backend.servicos.ServicoParaFeed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/perfil")
@AllArgsConstructor
@Slf4j
public class ControladorDePerfil {
    private ServicoParaPerfil servicoParaPerfil;
    private ServicoParaFeed servicoParaFeed;
    private ServicoDePagamentos servicoDePagamentos;

    @PostMapping("/publicar")
    public ResponseEntity<Void> publicar(@RequestBody RequisicaoDePostagem req, CustomServletWrapper request)  {
        log.info("Publicação - usuario - " + request.getHeader(Cabecalhos.USUARIO.getValue()));
        boolean publicar = servicoParaPerfil.publicar(
                req,
                DadosDeCabecalhos.builder()
                        .idUsuario(Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue())))
                        .idPerfilPrincipal(Long.parseLong(request.getHeader(Cabecalhos.PERFIL.getValue()).toString()))
                        .email(request.getHeader(Cabecalhos.EMAIL.getValue()))
                        .build()
        );
        if (publicar){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(405).build();
    }
    @PostMapping("criarperfil")
    public ResponseEntity<Void> criarPerfil(@RequestBody RequisicaoCriacaoPerfil dto, CustomServletWrapper request) {
        try {
            long l = Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue()));
            servicoParaPerfil.atualizarPerfil(dto,l);
            return ResponseEntity.status(200).build();
        } catch (NomeDeUsuarioExistente e) {
            return ResponseEntity.status(409).build();
        }
    }
    @PostMapping("editar")
    public ResponseEntity<Void> editarPerfil(@RequestBody RequisicaoCriacaoPerfil dto, CustomServletWrapper request) {
        try {
            long l = Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue()));
            servicoParaPerfil.atualizarPerfil(dto,l);
            return ResponseEntity.status(200).build();
        } catch (NomeDeUsuarioExistente e) {
            return ResponseEntity.status(409).build();
        }
    }
    @GetMapping("/obter")
    public ResponseEntity<PerfilDto> obter(CustomServletWrapper request) throws JsonProcessingException {
        log.info("Publicação - usuario - " + request.getHeader(Cabecalhos.USUARIO.getValue()));
        PerfilDto perfilDto = servicoParaPerfil.obterPerfil(Long.parseLong(request.getHeader(Cabecalhos.PERFIL.getValue())));
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(perfilDto);
        return ResponseEntity.ok().body(perfilDto);
    }
    @GetMapping("/obtertelaedicao")
    public ResponseEntity<PerfilDto> obtertelaedicao(CustomServletWrapper request) throws JsonProcessingException {
        PerfilDto perfilDto = servicoParaPerfil.obterPerfil(Long.parseLong(request.getHeader(Cabecalhos.PERFIL.getValue())));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(perfilDto);
        System.out.println(json);

        return ResponseEntity.ok().body(perfilDto);
    }
    @GetMapping("/obterpublicacoes")
    public ResponseEntity<RespostaPublicacoes> obterPublicaoes(CustomServletWrapper request)  {
        DadosDeCabecalhos dadosDeCabecalhos =  DadosDeCabecalhos.builder()
                .idPerfilPrincipal(Long.parseLong(request.getHeader(Cabecalhos.PERFIL.getValue()).toString()))
                .idUsuario(Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue()).toString()))
                .build();
        RespostaPublicacoes respostaFeed = servicoParaFeed.buscarPublicacoesDePerfil(dadosDeCabecalhos);

        return ResponseEntity.ok().body(respostaFeed);
    }
    @PostMapping("/salvar")
    public ResponseEntity<PerfilDto> salvarPerfil(@RequestBody RequisicaoSalvarPerfil req, CustomServletWrapper request) throws JsonProcessingException {
        log.info("Publicação - usuario - " + request.getHeader(Cabecalhos.USUARIO.getValue()));
        Boolean valido = servicoParaPerfil.salvarPerfil(DadosDeCabecalhos.builder()
                .idPerfilPrincipal(Long.parseLong(request.getHeader(Cabecalhos.PERFIL.getValue()))).build(), req);
        if (valido){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("trocarimagem")
    public ResponseEntity<Void> editarImagemPerfil(@RequestBody ReqEdicaoImagem dto, CustomServletWrapper request){
        boolean editou = servicoParaPerfil.editarImagem(dto,
                DadosDeCabecalhos.builder()
                        .idPerfilPrincipal(
                                Long.parseLong(request.getHeader(Cabecalhos.PERFIL.getValue())))
                        .build());
        if(editou){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.status(500).build();
        }
    }
    @PostMapping("/adicionarabiblioteca")
    public ResponseEntity<Void> adicionarABiblioteca(@RequestBody RequisicaoAdicionarABiblioteca req, CustomServletWrapper request) throws JsonProcessingException {
        log.info("Publicação - usuario - " + request.getHeader(Cabecalhos.USUARIO.getValue()));
        servicoParaPerfil.adicionarABiblioteca(req.getIdPublicacao(),Long.parseLong(request.getHeader(Cabecalhos.PERFIL.getValue()).toString()));

        return ResponseEntity.ok().build();
    }
}
