package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReqPrimeiraCompra {

    private String endereco;
    private String numero;
    private String cep;
    private String complemento;
    private String rotulo;
    private String cidade;
    private String estado;
}
