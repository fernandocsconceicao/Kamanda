package br.app.camarada.backend.controladores;

import br.app.camarada.backend.dto.DadosDeCabecalhos;
import br.app.camarada.backend.dto.RequisicaoCriacaoPerfil;
import br.app.camarada.backend.dto.ServicoParaPerfil;
import br.app.camarada.backend.dto.publicacao.req.RequisicaoDePostagem;
import br.app.camarada.backend.enums.Cabecalhos;
import br.app.camarada.backend.exception.NomeDeUsuarioExistente;
import br.app.camarada.backend.filtros.CustomServletWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            servicoParaPerfil.atualizarPerfil(dto.getNome(), dto.getTelefone(), dto.getNomeUsuario(),l);
            return ResponseEntity.status(200).build();
        } catch (NomeDeUsuarioExistente e) {
            return ResponseEntity.status(409).build();
        }
    }

    @PostMapping("/publicar")
    public ResponseEntity<Void> publicar(@RequestBody RequisicaoDePostagem req, CustomServletWrapper request) {
        log.info("Publicação - usuario - " + request.getHeader(Cabecalhos.USUARIO.getValue()));
        servicoParaPerfil.publicar(
                req,
                DadosDeCabecalhos.builder()
                        .idPerfilPrincipal(Long.parseLong(request.getHeader(Cabecalhos.PERFIL.getValue()).toString()))
                        .build()
        );
        return ResponseEntity.ok().build();
    }
}
