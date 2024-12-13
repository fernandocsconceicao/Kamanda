package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequisicaoSalvarPerfil {
    private String nomeDeUsuario;
    private String descricao;
    private String nome;
    private String telefone;
    private Integer idade;
    private String emailComercial;
    private String chavePix;
}
