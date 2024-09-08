package br.app.camarada.backend.dto;


import br.app.camarada.backend.dto.postagem.req.RequisicaoDePostagem;
import br.app.camarada.backend.entidades.Perfil;
import br.app.camarada.backend.entidades.Publicacao;
import br.app.camarada.backend.repositorios.RepositorioDePerfil;
import br.app.camarada.backend.repositorios.RepositorioDePublicacoes;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ServicoParaPerfil {
    private RepositorioDePublicacoes repositorioDePostagens;
    private RepositorioDePerfil repositorioDePerfil;

    public void publicar(RequisicaoDePostagem requisicaoDePostagem, DadosDeCabecalhos dadosUsuario){
        Publicacao publicacao = null;
        Optional<Perfil> perfilPessoal = repositorioDePerfil.findById(dadosUsuario.getIdPerfilPessoal());
        if(perfilPessoal.isPresent()){
            List<Perfil> perfisMencionados = new ArrayList<>();
            requisicaoDePostagem.getIdsDePerfisMencionados().forEach(id-> {
                Optional<Perfil> optional = repositorioDePerfil.findById(id);
                if(optional.isPresent()){
                    perfisMencionados.add(optional.get());
                }
            } );
            publicacao = Publicacao.montar(
                    requisicaoDePostagem.getTexto(),
                    requisicaoDePostagem.getTipoPostagem(),
                    perfisMencionados,
                    perfilPessoal.get()
                    );
        }
        repositorioDePostagens.save(publicacao);
    }
}
