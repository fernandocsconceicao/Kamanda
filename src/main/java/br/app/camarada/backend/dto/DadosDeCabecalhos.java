package br.app.camarada.backend.dto;

import br.app.camarada.backend.enums.TipoConta;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DadosDeCabecalhos {
    private TipoConta tipoConta;
    private Long idUsuario;
    private Long idPerfilPrincipal;
    private String email;
    private Boolean primeiroAcesso;
    private Long idRegiao;
    private Long idEstabecimento;
    private Long idEndereco;
    private Boolean primeiraCompra;
}
