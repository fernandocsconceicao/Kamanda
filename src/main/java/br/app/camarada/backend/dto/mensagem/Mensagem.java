package br.app.camarada.backend.dto.mensagem;

import br.app.camarada.backend.enums.Remetente;
import br.app.camarada.backend.enums.StatusEntregador;
import br.app.camarada.backend.enums.TipoMensagem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Mensagem {

    private TipoMensagem tipoMensagem;
    private BigDecimal valorCorrida;
    private String enderecoEstabelecimento;
    private String bairroEstabelecimento;
    private String enderecoClienteFinal;
    private String bairroClienteFinal;
    private String idEntregador;
    private String status;
    private Remetente remetente;
    private Long idPedido;
    private Boolean resposta;
    private Long idUsuario;
    private StatusEntregador statusEntregador;
    private String textoStatus;

}
