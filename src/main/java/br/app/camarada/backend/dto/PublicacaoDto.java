package br.app.camarada.backend.dto;

import br.app.camarada.backend.entidades.Perfil;
import br.app.camarada.backend.enums.TipoPostagem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PublicacaoDto {
    private String texto;
    private TipoPostagem tipoPostagem;
    private PerfilDto autorPrincipal;
    private List<Perfil> mencionados;
    private String resumo;
}
