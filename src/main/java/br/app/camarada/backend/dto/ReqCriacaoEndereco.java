package br.app.camarada.backend.dto;

import lombok.Data;

@Data
public class ReqCriacaoEndereco {

    private String rotulo;
    private String endereco;
    private String numero;
    private String complemento;
    private Boolean favorito;
    private String cep;
    private String cidade;
    private String estado;


}

