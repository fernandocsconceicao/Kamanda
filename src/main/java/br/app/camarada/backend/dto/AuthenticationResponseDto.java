package br.app.camarada.backend.dto;


import br.app.camarada.backend.enums.PlanoEstabelecimento;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponseDto {
    private String token;
    private String tipoConta;
    private Boolean primeiroAcesso;
    private Long usuarioId;
    private byte[] imagemPerfil;
    private Boolean emailConfirmado;
    private Long idEntregador;
    private Long idEstabelecimento;
//    private PlanoEstabelecimento plano;
}
