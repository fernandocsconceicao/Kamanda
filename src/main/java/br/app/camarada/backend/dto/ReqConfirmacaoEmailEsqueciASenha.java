package br.app.camarada.backend.dto;

import lombok.Data;

@Data
public class ReqConfirmacaoEmailEsqueciASenha {
    private String codigo;
    private String email;
}
