package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqEdicaoEndereco {
    private String endereco;
    private String numero;
    private String complemento;
    private Boolean favorito;
    private String rotulo;
    private String cidade;
    private String estado;
    private String cep;
}
