package br.app.camarada.backend.dto;


import br.app.camarada.backend.client.NotionClient;
import br.app.camarada.backend.client.WorldTimeClient;
import br.app.camarada.backend.dto.publicacao.req.RequisicaoDePostagem;
import br.app.camarada.backend.entidades.Perfil;
import br.app.camarada.backend.entidades.Publicacao;
import br.app.camarada.backend.entidades.PublicacaoDePropaganda;
import br.app.camarada.backend.entidades.Usuario;
import br.app.camarada.backend.enums.CategoriaPublicacao;
import br.app.camarada.backend.enums.StatusPropaganda;
import br.app.camarada.backend.enums.TipoPerfil;
import br.app.camarada.backend.exception.ErroPadrao;
import br.app.camarada.backend.exception.NomeDeUsuarioExistente;
import br.app.camarada.backend.repositorios.RepositorioDePerfil;
import br.app.camarada.backend.repositorios.RepositorioDePublicacoes;
import br.app.camarada.backend.repositorios.RepositorioDePublicacoesDePropaganda;
import br.app.camarada.backend.repositorios.RepositorioDeUsuario;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private RepositorioDePublicacoes repositorioDePublicacoes;
    private RepositorioDePublicacoesDePropaganda repositorioDePublicacoesDePropaganda;
    private RepositorioDePerfil repositorioDePerfil;
    private RepositorioDeUsuario repositorioDeUsuario;
    private WorldTimeClient worldTimeClient;
    private NotionClient notionClient;

    public boolean publicar(RequisicaoDePostagem dto, DadosDeCabecalhos dadosUsuario)  {
        Publicacao publicacao = null;
        Optional<Perfil> perfilPessoal = repositorioDePerfil.findById(dadosUsuario.getIdPerfilPrincipal());
        Usuario usuario = repositorioDeUsuario.findById(dadosUsuario.getIdUsuario()).get();
        if(!usuario.getPodePublicar()){
            return false;
        }
        if (perfilPessoal.isPresent()) {
            List<Perfil> perfisMencionados = new ArrayList<>();
            if(usuario.getContaDePropaganda()){
                String categoriaPublicacao = dto.getCategoria().obterValor();

                repositorioDePublicacoesDePropaganda.save(PublicacaoDePropaganda.builder()
                        .id(null)
                        .texto(dto.getTexto())
                        .data(LocalDateTime.now())
                        .tipoPublicacao(dto.getTipoPublicacao())
                        .autorPrincipal(perfilPessoal.get())
                        .categoriaPublicacao((CategoriaPublicacao.valueOf(categoriaPublicacao)))
                        .data(LocalDateTime.now())
                        .resumo(dto.getResumo())
                        .imagem(dto.getImagem())
                        .visualizacoes(0)
                        .naBibliotecaDePessoas(0)
                        .curtidas(0)
                        .manchete(dto.getManchete())
                        .idPerfil(dadosUsuario.getIdPerfilPrincipal())
                        .categoriasDaPropaganda("{}")
                        .propaganda(true)
                        .statusPropaganda(StatusPropaganda.ATIVA)
                        .build());
                return true;
            }
            if (dto.getIdsDePerfisMencionados() != null)
                dto.getIdsDePerfisMencionados().forEach(id -> {
                    Optional<Perfil> optional = repositorioDePerfil.findById(id);
                    if (optional.isPresent()) {
                        perfisMencionados.add(optional.get());
                    }
                });
            LocalDateTime data = LocalDateTime.now().minusHours(3);

            publicacao = Publicacao.montar(
                    dto.getTexto(),
                    dto.getTipoPublicacao(),
                    perfilPessoal.get(),
                    data,
                    dto.getResumo(),
                    dto.getImagem(),
                    dto.getManchete(),
                    perfilPessoal.get().getId(),
                    dto.getCategoria()
            );
            repositorioDePublicacoes.save(publicacao);
            ArrayList<String> tags = new ArrayList<>();
            tags.add(dadosUsuario.getEmail());
//            notionClient.tabularPublicacao("2022-06-28",
//                    "Bearer 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",
//                    RequisicaoAppendNotionBlock.construirPublicacaoEmDatabase(
//                            "128dd3360f128021af59c0941050cd4b",
//                            dto.getTexto().substring(0,
//                                    dto.getTexto().length()),
//                            tags,
//                            dto.getTexto() + "- " + dadosUsuario.getEmail()));
            return true;

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

    public Perfil atualizarPerfil(RequisicaoCriacaoPerfil dto, Long idUsuario) throws NomeDeUsuarioExistente {
        Optional<Perfil> optUsuario = repositorioDePerfil.findByNomeUsuario(dto.getNomeUsuario());

        if (optUsuario.isPresent()) {
            throw new NomeDeUsuarioExistente("Nome de usuario já está sendo utilizado");
        }

        Perfil perfil = new Perfil();

        if (dto.getNome() != null && !dto.getNome().isBlank() && !dto.getNome().equals(perfil.getNome())) {
            perfil.setNome(dto.getNome());
        }
        if (dto.getTelefone() != null && !dto.getTelefone().isBlank() && !dto.getTelefone().equals(perfil.getTelefone())) {
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
        Usuario usuario = repositorioDeUsuario.findById(idUsuario).get();
        usuario.setPrimeiroAcesso(false);
        repositorioDeUsuario.save(usuario);

        return perfil;
    }

    public void excluirPerfil(Long usuarioId) {
        repositorioDePerfil.deleteByUsuarioId(usuarioId);
    }

    public PerfilDto obterPerfil(Long idPerfil) {
        Optional<Perfil> optPerfil = repositorioDePerfil.findById(idPerfil);
        if (optPerfil.isPresent()) {
            Perfil perfil = optPerfil.get();
            return PerfilDto.montar(perfil);
        } else {
            throw new ErroPadrao("Perfil não encontrado");
        }

    }

    public void adicionarABiblioteca(Long idPublicacao, Long idPerfil) throws JsonProcessingException {
        Perfil perfil = repositorioDePerfil.findById(idPerfil).get();

        String minhaBibliotecaJson = perfil.getMinhaBibliotecaJson();
        ObjectMapper objMapper = new ObjectMapper();
        List<Long> idsBiblioteca;
        if (minhaBibliotecaJson == null) {
            idsBiblioteca = new ArrayList<>();
        } else {
            idsBiblioteca = objMapper.readValue(minhaBibliotecaJson, new TypeReference<List<Long>>() {
            });
        }

        if (repositorioDePublicacoes.findById(idPublicacao).isPresent()) {
            idsBiblioteca.add(idPublicacao);
            perfil.setMinhaBibliotecaJson(objMapper.writeValueAsString(idsBiblioteca));
            repositorioDePerfil.save(perfil);
        }


    }

    public Boolean salvarPerfil(DadosDeCabecalhos dadosDeCabecalhos,RequisicaoSalvarPerfil req) {
        Long idPerfilPrincipal = dadosDeCabecalhos.getIdPerfilPrincipal();
        Optional<Perfil> optional = repositorioDePerfil.findById(idPerfilPrincipal);
        if(optional.isPresent()){
            Perfil perfil = optional.get();
            perfil.setTelefone(req.getTelefone());
            perfil.setNomeUsuario(req.getNomeDeUsuario());
            perfil.setNome(req.getNome());
            perfil.setIdade(req.getIdade());
            perfil.setEmailComercial(req.getEmailComercial());
            perfil.setChavePix(req.getChavePix());
            perfil.setResumo(req.getDescricao());

            repositorioDePerfil.save(perfil);
            return true;
        }
        return false;
    }

    public boolean editarImagem(ReqEdicaoImagem dto,DadosDeCabecalhos dadosDeCabecalhos) {
        Optional<Perfil> perfil = repositorioDePerfil.findById(dadosDeCabecalhos.getIdPerfilPrincipal());
        if(perfil.isPresent()){
            Perfil perfilObj = perfil.get();
            perfilObj.setImagem(dto.getImagem());
            repositorioDePerfil.save(perfilObj);
            return true;
        }else {
            return false;
        }
    }
}
