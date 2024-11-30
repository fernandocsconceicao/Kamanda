package br.app.camarada.backend.dto;


import br.app.camarada.backend.client.NotionClient;
import br.app.camarada.backend.client.WorldTimeClient;
import br.app.camarada.backend.dto.notion.RequisicaoAppendNotionBlock;
import br.app.camarada.backend.dto.publicacao.req.RequisicaoDePostagem;
import br.app.camarada.backend.entidades.Perfil;
import br.app.camarada.backend.entidades.Publicacao;
import br.app.camarada.backend.entidades.Usuario;
import br.app.camarada.backend.enums.TipoPerfil;
import br.app.camarada.backend.exception.ErroPadrao;
import br.app.camarada.backend.exception.NomeDeUsuarioExistente;
import br.app.camarada.backend.filtros.CustomServletWrapper;
import br.app.camarada.backend.repositorios.RepositorioDePerfil;
import br.app.camarada.backend.repositorios.RepositorioDePublicacoes;
import br.app.camarada.backend.repositorios.RepositorioDeUsuario;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    private NotionClient  notionClient;

    public void publicar(RequisicaoDePostagem requisicaoDePostagem, DadosDeCabecalhos dadosUsuario) throws JsonProcessingException {
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
            LocalDateTime data ;
                    try{
                        data = worldTimeClient.buscarHora().getDatetime().toLocalDateTime();
                    }catch (Exception e){

                        data = LocalDateTime.now().minusHours(3);
                    }
            publicacao = Publicacao.montar(
                    requisicaoDePostagem.getTexto(),
                    requisicaoDePostagem.getTipoPostagem(),
                    perfilPessoal.get(),
                    data,
                    requisicaoDePostagem.getResumo()
            );
            repositorioDePostagens.save(publicacao);
            ArrayList<String> tags = new ArrayList<>();
            tags.add(dadosUsuario.getEmail());
            notionClient.tabularPublicacao("2022-06-28",
                    "Bearer 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",
                    RequisicaoAppendNotionBlock.construirPublicacaoEmDatabase(
                    "128dd3360f128021af59c0941050cd4b",
                    requisicaoDePostagem.getTexto().substring(0,requisicaoDePostagem.getTexto().length()),tags,requisicaoDePostagem.getTexto()+ "- "+ dadosUsuario.getEmail()));

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
    public Perfil atualizarPerfil( RequisicaoCriacaoPerfil dto,Long idUsuario) throws NomeDeUsuarioExistente {
        Optional<Perfil> optUsuario = repositorioDePerfil.findByNomeUsuario(dto.getNomeUsuario());

        if(optUsuario.isPresent()){
            throw new NomeDeUsuarioExistente("Nome de usuario já está sendo utilizado");
        }

        Perfil perfil = new Perfil();

        if(dto.getNome()!= null && !dto.getNome().isBlank() && !dto.getNome().equals(perfil.getNome())){
            perfil.setNome(dto.getNome());
        }
        if(dto.getTelefone()!= null && !dto.getTelefone().isBlank() && !dto.getTelefone().equals(perfil.getTelefone())){
            perfil.setTelefone(dto.getTelefone());
        }
        perfil.setTipoPerfil(TipoPerfil.PESSOAL);
        perfil.setNomeUsuario(dto.getNomeUsuario().toLowerCase());
        perfil.setNome(dto.getNome());
        perfil.setTelefone(dto.getTelefone());
        perfil.setVerificado(false);
        perfil.setImagem(dto.getImagemPerfil());
        perfil.setImagemFundo(dto.getImagemPerfil());
        repositorioDePerfil.save(perfil);
        Usuario usuario= repositorioDeUsuario.findById(idUsuario).get();
        usuario.setPrimeiroAcesso(false);
        repositorioDeUsuario.save(usuario);

        return perfil;
    }

    public void excluirPerfil(Long usuarioId) {
        repositorioDePerfil.deleteByUsuarioId(usuarioId);
    }

    public PerfilDto obterPerfil(Long idPerfil) {
        Optional<Perfil> optPerfil = repositorioDePerfil.findById(idPerfil);
        if (optPerfil.isPresent()){
            Perfil perfil = optPerfil.get();
            return PerfilDto.montar(perfil);
        }else {
            throw new ErroPadrao("Perfil não encontrado");
        }

    }
}
