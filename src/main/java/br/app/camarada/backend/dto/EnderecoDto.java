package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EnderecoDto {
    private Long id;
    private String endereco;
    private String numero;
    private String complemento;
    private String enderecoCompleto;
    private String cep;
    private String rotulo;
    private String cidade;
    private String estado;
}
