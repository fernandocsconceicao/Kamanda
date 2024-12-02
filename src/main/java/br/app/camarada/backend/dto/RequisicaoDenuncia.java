package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequisicaoDenuncia {
    private Long idPublicacao;
    private String motivo;
    private String descricao;
    private Long idPerfil;
}
