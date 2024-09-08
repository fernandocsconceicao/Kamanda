package br.app.camarada.backend.dto;

import br.app.camarada.backend.entidades.Perfil;
import br.app.camarada.backend.enums.TipoPerfil;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PerfilDto {
    private String nome;
    private Boolean verificado;
    private TipoPerfil tipoPerfil;

    public static PerfilDto montar(Perfil perfil) {
        return new PerfilDto(perfil.getNome(), perfil.getVerificado(),perfil.getTipoPerfil());
    }
}
