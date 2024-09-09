package br.app.camarada.backend.controladores;

import br.app.camarada.backend.dto.AuthenticationResponseDto;
import br.app.camarada.backend.dto.CredenciaisDeExclusaoDeConta;
import br.app.camarada.backend.dto.RequisicaoDeAutenticacao;
import br.app.camarada.backend.dto.RequisicaoRegistro;
import br.app.camarada.backend.exception.EmailJaCadastradoException;
import br.app.camarada.backend.servicos.ServicoParaUsuarios;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
@AllArgsConstructor
public class ControladorDeUsuarios {
    private ServicoParaUsuarios servicoParaUsuarios;

    @PostMapping("/registrar")
    public ResponseEntity<AuthenticationResponseDto> registrarUsuario(@RequestBody RequisicaoRegistro dto) {
        try {
            AuthenticationResponseDto authenticationResponseDto = servicoParaUsuarios.registrarUsuario(dto);
            if (authenticationResponseDto == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.ok().body(authenticationResponseDto);
        } catch (EmailJaCadastradoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }


    }

    @PostMapping("/autenticar")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody RequisicaoDeAutenticacao dto) {
        AuthenticationResponseDto authenticate = servicoParaUsuarios.authenticate(dto);
        return ResponseEntity.ok().body(authenticate);
    }

    @PostMapping("excluir")
    public ResponseEntity<Void> excluirUsuario(@RequestBody CredenciaisDeExclusaoDeConta dto) {
        try {
            AuthenticationResponseDto authenticate = servicoParaUsuarios.authenticate(
//                    new RequisicaoDeAutenticacao("silvioalmeida@hotmail.com", "silvioalmeida")
                    new RequisicaoDeAutenticacao(dto.getEmail(), dto.getSenha())
            );

            if (authenticate != null) {
                servicoParaUsuarios.excluirConta(authenticate.getUsuarioId());
            } else {
                return ResponseEntity.status(404).build();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }
}
