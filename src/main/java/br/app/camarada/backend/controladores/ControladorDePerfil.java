package br.app.camarada.backend.controladores;

import br.app.camarada.backend.dto.*;
import br.app.camarada.backend.dto.publicacao.req.RequisicaoDePostagem;
import br.app.camarada.backend.enums.Cabecalhos;
import br.app.camarada.backend.exception.NomeDeUsuarioExistente;
import br.app.camarada.backend.filtros.CustomServletWrapper;
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
        System.out.println(json);
        return ResponseEntity.ok().body(perfilDto);
    }
    @PostMapping("/publicar")
    public ResponseEntity<Void> publicar(@RequestBody RequisicaoDePostagem req, CustomServletWrapper request) throws JsonProcessingException {
        log.info("Publicação - usuario - " + request.getHeader(Cabecalhos.USUARIO.getValue()));
        servicoParaPerfil.publicar(
                req,
                DadosDeCabecalhos.builder()
                        .idPerfilPrincipal(Long.parseLong(request.getHeader(Cabecalhos.PERFIL.getValue()).toString()))
                        .email(request.getHeader(Cabecalhos.EMAIL.getValue()))
                        .build()
        );
        return ResponseEntity.ok().build();
    }
}
