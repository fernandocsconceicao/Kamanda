package br.app.camarada.backend.dto;

import br.app.camarada.backend.entidades.Perfil;
import br.app.camarada.backend.enums.RotulosPerfil;
import br.app.camarada.backend.enums.TextosBundle;
import br.app.camarada.backend.enums.TipoBlocoPerfil;
import br.app.camarada.backend.enums.TipoPerfil;
import br.app.camarada.backend.utilitarios.UtilitarioBundle;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class PerfilPublicacaoDto {
    private Long id;
    private String nome;
    private Boolean verificado;
    private String nomeUsuario;
    private byte[] miniatura;

    public static PerfilPublicacaoDto montar(Perfil perfil) {


        return new PerfilPublicacaoDto(perfil.getId(), perfil.getNome(), perfil.getVerificado(), perfil.getNomeUsuario(), perfil.getImagem());
    }
}
