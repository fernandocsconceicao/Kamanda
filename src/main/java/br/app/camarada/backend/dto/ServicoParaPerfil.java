package br.app.camarada.backend.dto;


import br.app.camarada.backend.client.WorldTimeClient;
import br.app.camarada.backend.dto.publicacao.req.RequisicaoDePostagem;
import br.app.camarada.backend.entidades.Perfil;
import br.app.camarada.backend.entidades.Publicacao;
import br.app.camarada.backend.entidades.Usuario;
import br.app.camarada.backend.enums.TipoPerfil;
import br.app.camarada.backend.exception.NomeDeUsuarioExistente;
import br.app.camarada.backend.repositorios.RepositorioDePerfil;
import br.app.camarada.backend.repositorios.RepositorioDePublicacoes;
import br.app.camarada.backend.repositorios.RepositorioDeUsuario;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.MalformedParametersException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ServicoParaPerfil {
    private RepositorioDePublicacoes repositorioDePostagens;
    private RepositorioDePerfil repositorioDePerfil;
    private RepositorioDeUsuario repositorioDeUsuario;
    private WorldTimeClient worldTimeClient;

    public void publicar(RequisicaoDePostagem requisicaoDePostagem, DadosDeCabecalhos dadosUsuario) {
        Publicacao publicacao = null;
        Optional<Perfil> perfilPessoal = repositorioDePerfil.findById(dadosUsuario.getIdPerfilPrincipal());

        if (perfilPessoal.isPresent()) {
            List<Perfil> perfisMencionados = new ArrayList<>();
            if (requisicaoDePostagem.getIdsDePerfisMencionados() != null)
                requisicaoDePostagem.getIdsDePerfisMencionados().forEach(id -> {
                    Optional<Perfil> optional = repositorioDePerfil.findById(id);
                    if (optional.isPresent()) {
                        perfisMencionados.add(optional.get());
                    }
                });
            LocalDateTime data = worldTimeClient.buscarHora().getDatetime().toLocalDateTime();
            publicacao = Publicacao.montar(
                    requisicaoDePostagem.getTexto(),
                    requisicaoDePostagem.getTipoPostagem(),
                    perfisMencionados,
                    perfilPessoal.get(),
                    data
            );
            repositorioDePostagens.save(publicacao);
        } else {
            throw new MalformedParametersException();
        }

    }

    public Perfil criarPerfil(Usuario user) {
        Perfil perfil = new Perfil();

        perfil.setTipoPerfil(TipoPerfil.PESSOAL);
        perfil.setUsuario(user);
        perfil.setNome(user.getNome());
        perfil.setVerificado(false);

        return repositorioDePerfil.save(perfil);

    }
    public Perfil atualizarPerfil( String nomeUsuario, String telefone, String nome,Long idUsuario) throws NomeDeUsuarioExistente {
        Optional<Perfil> optUsuario = repositorioDePerfil.findByNomeUsuario(nomeUsuario);

        if(optUsuario.isPresent()){
            throw new NomeDeUsuarioExistente("Nome de usuario já está sendo utilizado");
        }

        Perfil perfil = new Perfil();

        perfil.setTipoPerfil(TipoPerfil.PESSOAL);
        perfil.setNomeUsuario(nomeUsuario.toLowerCase());
        perfil.setNome(nome);
        perfil.setTelefone(telefone);
        perfil.setVerificado(false);
        repositorioDePerfil.save(perfil);
        Usuario usuario= repositorioDeUsuario.findById(idUsuario).get();
        usuario.setPrimeiroAcesso(false);
        repositorioDeUsuario.save(usuario);

        return perfil;
    }

    public void excluirPerfil(Long usuarioId) {
        repositorioDePerfil.deleteByUsuarioId(usuarioId);
    }
}
