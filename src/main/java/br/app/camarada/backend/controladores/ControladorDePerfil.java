package br.app.camarada.backend.controladores;

import br.app.camarada.backend.dto.DadosDeCabecalhos;
import br.app.camarada.backend.dto.ServicoParaPerfil;
import br.app.camarada.backend.dto.publicacao.req.RequisicaoDePostagem;
import br.app.camarada.backend.enums.Cabecalhos;
import br.app.camarada.backend.filtros.CustomServletWrapper;
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

    @PostMapping("/publicar")
    public ResponseEntity<Void> publicar(@RequestBody RequisicaoDePostagem req, CustomServletWrapper request){
        log.info("Publicação - usuario - "+ request.getHeader(Cabecalhos.USUARIO.getValue()));
        servicoParaPerfil.publicar(
                req,
                DadosDeCabecalhos.builder()
                        .idPerfilPrincipal( Long.parseLong(request.getHeader(Cabecalhos.PERFIL.getValue()).toString()))
                        .build()
               );
        return ResponseEntity.ok().build();
    }
}
