package br.app.camarada.backend.dto;

import br.app.camarada.backend.enums.TipoServico;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SolicitacaoDeEntrega {
    private String enderecoInicial;
    private String enderecoFinal;
    private String conteudo;
    private String nomeCliente;
    private TipoServico tipoServico;

}
