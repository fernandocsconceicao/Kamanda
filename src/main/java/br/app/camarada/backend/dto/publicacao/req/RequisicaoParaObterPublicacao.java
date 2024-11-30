package br.app.camarada.backend.dto.publicacao.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequisicaoParaObterPublicacao {
    private Long idPublicacao;
}
