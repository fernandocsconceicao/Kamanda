package br.app.camarada.backend.dto;

import lombok.Data;

@Data
public class RequisicaoDefinicaoLocalidade {
    private String enderecoCompleto;
    private Long idCliente;
    private Long idEstabelecimento;
}
