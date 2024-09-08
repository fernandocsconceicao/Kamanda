package br.app.camarada.backend.controladores;

import br.app.camarada.backend.dto.AuthenticationResponseDto;
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
            System.out.println("23423");
            AuthenticationResponseDto authenticationResponseDto = servicoParaUsuarios.registrarUsuario(dto);
            if (authenticationResponseDto == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.ok().body(authenticationResponseDto);
        } catch (EmailJaCadastradoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }


    }
}
