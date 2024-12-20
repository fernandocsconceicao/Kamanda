package br.app.camarada.backend.controladores;

import br.app.camarada.backend.dto.RequisicaoDefinicaoLocalidade;
import br.app.camarada.backend.dto.ResListagemDeRegioes;
import br.app.camarada.backend.servicos.ServicoDeRegiao;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("regiao")
public class RegiaoController {
    private ServicoDeRegiao servicoDeRegiao;


    @GetMapping("listar")
    public ResponseEntity<ResListagemDeRegioes> listarRegioes() {
        return ResponseEntity.ok().body(new ResListagemDeRegioes(servicoDeRegiao.listar()));
    }

    @PostMapping("/definirlocalidade")
    public ResponseEntity<String> definirLocalidade(@RequestBody RequisicaoDefinicaoLocalidade dto) {

        return ResponseEntity.ok().body("placeID: ");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> tokenExpirado() {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Token Expirado");

    }

}
