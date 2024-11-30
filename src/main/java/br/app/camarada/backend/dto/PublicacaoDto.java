package br.app.camarada.backend.dto;

import br.app.camarada.backend.entidades.Perfil;
import br.app.camarada.backend.enums.TipoPostagem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PublicacaoDto {
    private Long id;
    private TipoPostagem tipoPostagem;
    private PerfilDto autorPrincipal;
    private String resumo;
    private String data;
    private String texto;
}
