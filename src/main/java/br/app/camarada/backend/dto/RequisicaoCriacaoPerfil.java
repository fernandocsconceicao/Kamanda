package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequisicaoCriacaoPerfil {
    private String nomeUsuario;
    private String nome;
    private String telefone;
}
