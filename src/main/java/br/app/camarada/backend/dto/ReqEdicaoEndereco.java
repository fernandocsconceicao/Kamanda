package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqEdicaoEndereco {
    private Long id;
    private String endereco;
    private String numero;
    private String complemento;
    private String enderecoCompleto;
    private Boolean favorito;
    private String rotulo;
    private String cidade;
    private String estado;
    private String cep;
}
