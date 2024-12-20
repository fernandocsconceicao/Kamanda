package br.app.camarada.backend.controladores;

import br.app.camarada.backend.dto.*;
import br.app.camarada.backend.enums.Cabecalhos;
import br.app.camarada.backend.exception.EmailJaCadastradoException;
import br.app.camarada.backend.filtros.CustomServletWrapper;
import br.app.camarada.backend.servicos.ServicoDeAdministracao;
import br.app.camarada.backend.servicos.ServicoDePagamentos;
import br.app.camarada.backend.servicos.ServicoParaUsuarios;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@AllArgsConstructor
public class ControladorDeUsuarios {
    private ServicoParaUsuarios servicoParaUsuarios;
    private ServicoDeAdministracao servicoDeAdministracao;
    @PostMapping("/autenticar")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody RequisicaoDeAutenticacao dto
            , CustomServletWrapper request) throws JsonProcessingException {

        AuthenticationResponseDto authenticate = servicoParaUsuarios.authenticate(dto);
        System.out.println(new ObjectMapper().writeValueAsString(authenticate));
        return ResponseEntity.ok().body(authenticate);
    }
    @PostMapping("/endereco/editar")
    public ResponseEntity<Void> editarEndereco(@RequestBody ReqEdicaoEndereco dto, CustomServletWrapper request) {

        Long idEstabelecimento = null;
        if (request.getHeader(Cabecalhos.ESTABELECIMENTO.getValue()) != null) {
            idEstabelecimento = Long.parseLong(request.getHeader(Cabecalhos.ESTABELECIMENTO.getValue()));
        }
        servicoParaUsuarios.editarEndereco(dto,DadosDeCabecalhos.builder()
                .idUsuario(Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue())))
                .idEndereco(Long.parseLong(request.getHeader(Cabecalhos.ENDERECO.getValue())))
                .build());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/endereco/tela")
    public ResponseEntity<ResTelaEntrega> obterTelaDeEdicaoDeEndereco( CustomServletWrapper request) {

        EnderecoDto enderecoDto = servicoParaUsuarios.obterEndereco(DadosDeCabecalhos.builder()
                .idUsuario(Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue())))
                .build()
        );
        ResTelaEntrega resTelaEntrega = new ResTelaEntrega(enderecoDto);
        return ResponseEntity.ok().body(resTelaEntrega);
    }

    @PostMapping("/denunciar")
    public ResponseEntity<AuthenticationResponseDto> denunciar(@RequestBody RequisicaoDenuncia dto, CustomServletWrapper request) throws JsonProcessingException {
          servicoDeAdministracao.denuncia(dto,
                    DadosDeCabecalhos.builder()
                            .idPerfilPrincipal(Long.parseLong(request.getHeader(Cabecalhos.PERFIL.getValue()).toString()))
                            .email(request.getHeader(Cabecalhos.EMAIL.getValue()))
                            .build());

        return ResponseEntity.ok().build();

    }
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
    @PostMapping("/confirmaremail")
    public ResponseEntity confirmaremail(@RequestBody ReqConfirmacaoEmail dto, CustomServletWrapper request) {
        if (servicoParaUsuarios.confirmarEmail(dto.getCodigo(), Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue())))) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }
    @PostMapping("/autenticar/existeemail")
    public ResponseEntity<RespostaExisteEmailCadastrado> existeEmail(@RequestBody RequisicaoExisteEmailCadastrado dto) {
        Boolean resposta = servicoParaUsuarios.existeUsuarioCadastrado(dto.getEmail());
        return ResponseEntity.ok().body(new RespostaExisteEmailCadastrado(resposta));
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
