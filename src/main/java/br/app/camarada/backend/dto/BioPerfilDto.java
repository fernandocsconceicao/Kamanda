package br.app.camarada.backend.dto;

import br.app.camarada.backend.enums.TipoBlocoPerfil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class BioPerfilDto {
    private TipoBlocoPerfil tipoBlocoPerfil;
    private String atributo;
    private String valor;
    private Boolean valorBooleano;
    private Set<String> rotulos;
}
