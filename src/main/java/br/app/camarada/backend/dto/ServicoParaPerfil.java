package br.app.camarada.backend.dto;


import br.app.camarada.backend.dto.postagem.req.RequisicaoDePostagem;
import br.app.camarada.backend.entidades.Perfil;
import br.app.camarada.backend.entidades.Publicacao;
import br.app.camarada.backend.repositorios.RepositorioDePerfilPessoal;
import br.app.camarada.backend.repositorios.RepositorioDePostagens;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicoParaPerfil {
    private RepositorioDePostagens repositorioDePostagens;
    private RepositorioDePerfilPessoal repositorioDePerfilPessoal;

    public void publicar(RequisicaoDePostagem requisicaoDePostagem, DadosDeCabecalhos dadosUsuario){
        Publicacao publicacao = null;
        Optional<Perfil> perfilPessoal = repositorioDePerfilPessoal.findById(dadosUsuario.getIdPerfilPessoal());
        if(perfilPessoal.isPresent()){
            publicacao = Publicacao.montar(requisicaoDePostagem.getTexto(),
                    requisicaoDePostagem.getTipoPostagem(),
                    List.of(perfilPessoal.get())
            );
        }
        repositorioDePostagens.save(publicacao);
    }
}
