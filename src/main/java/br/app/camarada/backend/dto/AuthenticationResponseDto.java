package br.app.camarada.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponseDto {
    private String token;
    private Long totemId;
    private Long estabelecimentoId;
    private String accountType;
    private Boolean primeiroAcesso;
    private Long usuarioId;
    private byte[] imagemPerfil;
    private Boolean emailConfirmado;
    private Long idEntregador;
//    private PlanoEstabelecimento plano;
}
