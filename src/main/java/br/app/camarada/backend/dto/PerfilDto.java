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
public class PerfilDto {
    private Long id;
    private String nome;
    private Boolean verificado;
    private TipoPerfil tipoPerfil;
    private List<BioPerfilDto> bio;
    private String nomeUsuario;
    private String descricao;
    private String telefone;
    private String idade;
    private String chavePix;
    private String emailComercial;


    public static PerfilDto montar(Perfil perfil) {
        ArrayList<BioPerfilDto> bio = new ArrayList<>();

        if (perfil.getIdade() != null)
            bio.add(
                    new BioPerfilDto(TipoBlocoPerfil.ATRIBUTO_VALOR,
                            UtilitarioBundle.obterMensagem(TextosBundle.ROTULO_PERFIL_IDADE),
                            perfil.getIdade().toString(),
                            null,
                            null

                    ));
        if (perfil.getVerificado() != null && perfil.getVerificado()) {
            bio.add(
                    new BioPerfilDto(
                            TipoBlocoPerfil.ATRIBUTO_VALOR_BOOLEANO,
                            null,
                            UtilitarioBundle.obterMensagem(TextosBundle.VERIFICADO),
                            perfil.getVerificado(),
                            null
                    ));
        }
        if (perfil.getRotulosEscolhidos() != null) {
            Set<String> rotulos = new HashSet<>();

            if (perfil.getRotulosEscolhidos().isEmpty()) {
                Set<RotulosPerfil> objects = new HashSet<>();
                objects.add(RotulosPerfil.PILOTO);
                perfil.setRotulosEscolhidos(objects);
            }

            perfil.getRotulosEscolhidos().forEach(rotulosPerfil -> rotulos.add(rotulosPerfil.obterValor()));
            bio.add(
                    new BioPerfilDto(
                            TipoBlocoPerfil.ATRIBUTO_VALOR,
                            UtilitarioBundle.obterMensagem(TextosBundle.BIO),
                            perfil.getResumo(),
                            null,
                            rotulos
                    ));
            bio.add(
                    new BioPerfilDto(
                            TipoBlocoPerfil.ROTULOS,
                            UtilitarioBundle.obterMensagem(TextosBundle.QUERO_SER_CONHECIDO_POR),
                            null,
                            null,
                            rotulos
                    ));
        }
        return new PerfilDto(perfil.getId(), perfil.getNome(), perfil.getVerificado(), perfil.getTipoPerfil(), bio,
                perfil.getNomeUsuario(), perfil.getResumo(), perfil.getTelefone(),perfil.getIdade().toString(),
                perfil.getChavePix(), perfil.getEmailComercial());
    }
}
