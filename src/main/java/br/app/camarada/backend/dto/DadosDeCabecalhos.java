package br.app.camarada.backend.dto;

import br.app.camarada.backend.enums.TipoConta;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DadosDeCabecalhos {
    private TipoConta tipoConta;
    private Long idUsuario;
    private Long idPerfilPessoal;
}
