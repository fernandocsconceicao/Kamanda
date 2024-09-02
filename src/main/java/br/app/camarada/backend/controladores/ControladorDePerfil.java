package br.app.camarada.backend.controladores;

import br.app.camarada.backend.dto.DadosDeCabecalhos;
import br.app.camarada.backend.dto.ServicoParaPerfil;
import br.app.camarada.backend.dto.postagem.req.RequisicaoDePostagem;
import br.app.camarada.backend.enums.Cabecalhos;
import br.app.camarada.backend.filtros.CustomServletWrapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("perfil")
@AllArgsConstructor
public class ControladorDePerfil {
    private ServicoParaPerfil servicoParaPerfil;

    @GetMapping("publicar")
    public ResponseEntity<Void> publicar(@RequestBody RequisicaoDePostagem req, CustomServletWrapper request){

        servicoParaPerfil.publicar(
                req,
                DadosDeCabecalhos.builder()
                        .idPerfilPessoal( Long.parseLong(request.getHeader(Cabecalhos.PERFIL.getValue()).toString()))
                        .build()
               );
        return ResponseEntity.ok().build();
    }
}
