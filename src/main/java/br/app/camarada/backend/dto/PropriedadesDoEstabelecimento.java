package br.app.camarada.backend.dto;

import br.app.camarada.backend.enums.PlanoEstabelecimento;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PropriedadesDoEstabelecimento {
    private String nome;
    private String cnpj;
    private String telefone;
    private PlanoEstabelecimento planoEstabelecimento;
}
