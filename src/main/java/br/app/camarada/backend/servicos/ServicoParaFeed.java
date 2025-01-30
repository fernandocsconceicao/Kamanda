package br.app.camarada.backend.servicos;

import br.app.camarada.backend.dto.*;
import br.app.camarada.backend.dto.mercadopago.MercadoPagoPixResponse;
import br.app.camarada.backend.dto.publicacao.req.RequisicaoParaObterPublicacao;
import br.app.camarada.backend.dto.publicacao.res.RespostaPublicacoes;
import br.app.camarada.backend.entidades.*;
import br.app.camarada.backend.enums.*;
import br.app.camarada.backend.exception.ExcessaoDePagamentoCancelado;
import br.app.camarada.backend.repositorios.RepositorioDePerfil;
import br.app.camarada.backend.repositorios.RepositorioDePublicacoes;
import br.app.camarada.backend.repositorios.RepositorioDePublicacoesDePropaganda;
import br.app.camarada.backend.repositorios.RepositorioDeUsuario;
import br.app.camarada.backend.utilitarios.UtilitarioBundle;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ServicoParaFeed {
    private RepositorioDePublicacoes repositorioDePublicacoes;
    private RepositorioDePerfil repositorioDePerfil;
    private ServicoDePagamentos servicoDePagamentos;
    private RepositorioDePublicacoesDePropaganda repositorioDePublicacoesDePropaganda;
    private RepositorioDeUsuario repositorioDeUsuario;


    public RespostaFeed buscarPublicacoes(DadosDeCabecalhos dadosDeCabecalhos, RequisicaoFeed dto) {
        List<Pagamento> pagamentos = servicoDePagamentos.verificarPagamentosPendentes(dadosDeCabecalhos.getIdUsuario());
        Integer publicacoesPorSaidaDePublicacoes = 5;
        Integer propagandaPorSaidaDePublicacoes = 3;
        Integer componentesDePublicacoesPorSaida = publicacoesPorSaidaDePublicacoes + propagandaPorSaidaDePublicacoes;
        boolean pagamentosPendentes = false;
        String codigoPix = null;
        String tituloDoErro = null;
        String descricaoDoErro = null;
        TipoErro tipoErro = null;
        TipoServico tipoServico = null;
        Boolean podePublicar;

        if (pagamentos != null) {
            pagamentosPendentes = true;
            Pagamento pagamento = pagamentos.get(0);
            if (pagamento != null && pagamento.getFormaDePagamento().equals(FormaDePagamento.PIX)) {
                codigoPix = pagamento.getPixId().toString();
            }
            try {
                MercadoPagoPixResponse mercadoPagoPixResponse = servicoDePagamentos.getMercadoPagoPixResponse(codigoPix.toString());

                codigoPix = mercadoPagoPixResponse.id.toString();
                tituloDoErro = UtilitarioBundle.obterMensagem(TextosBundle.TITULO_ERRO_PAGAMENTO_PENDENTE_PIX);
                descricaoDoErro = UtilitarioBundle.obterMensagem(TextosBundle.DESCRICAO_ERRO_PAGAMENTO_PENDENTE_PIX);
                tipoErro = TipoErro.PAGAMENTO_EM_ANDAMENTO;
                tipoServico = servicoDePagamentos.obterPagamento(Long.parseLong(codigoPix)).getTipoServico();
            } catch (ExcessaoDePagamentoCancelado e) {
                tituloDoErro = UtilitarioBundle.obterMensagem(TextosBundle.TITULO_ERRO_PAGAMENTO_PENDENTE_E_CANCELADO_PIX);
                descricaoDoErro = UtilitarioBundle.obterMensagem(TextosBundle.DESCRICAO_ERRO_PAGAMENTO_PENDENTE_E_CANCELADO_PIX);
                tipoErro = TipoErro.INFORMATIVO;
            }
        }

        List<IPublicacao> publicacoes = new ArrayList<>();
        List<Publicacao> pbc;
        List<PublicacaoDePropaganda> pbcPropaganda = new ArrayList<>();
        if (dto.getCategoria() != null) {
            if (dto.getCategoria() == CategoriaPublicacao.TODAS) {
                pbc = repositorioDePublicacoes.obterPublicacoesSemCategoria(dto.getCategoria().obterEnumeracao());
                pbcPropaganda = repositorioDePublicacoesDePropaganda.obterPropagandasParaExibicao(4);

            } else {
                pbc = repositorioDePublicacoes.obterPorCategoria(dto.getCategoria().obterEnumeracao());
                if (pbc.isEmpty()) {
                    pbc = repositorioDePublicacoes.obterPublicacoesSemCategoria(publicacoesPorSaidaDePublicacoes);
                }
                pbcPropaganda = repositorioDePublicacoesDePropaganda.obterPropagandasParaExibicao(propagandaPorSaidaDePublicacoes);
            }
        } else {
            pbc = repositorioDePublicacoes.obterPublicacoesSemCategoria(publicacoesPorSaidaDePublicacoes);
            pbcPropaganda = repositorioDePublicacoesDePropaganda.obterPropagandasParaExibicao(propagandaPorSaidaDePublicacoes);
        }
        int indexpbc = 0;
        int indexpbcPropaganda = 0;
        boolean pegarPublicacao = true;

        for (int i = 0; i < componentesDePublicacoesPorSaida; i++) {
            if (pegarPublicacao && indexpbc < pbc.size()) {
                publicacoes.add(pbc.get(indexpbc));
                indexpbc++;
            } else if (!pegarPublicacao && indexpbcPropaganda < pbcPropaganda.size()) {
                publicacoes.add(pbcPropaganda.get(indexpbcPropaganda));
                indexpbcPropaganda++;
            }

            pegarPublicacao = !pegarPublicacao;
        }

        List<PublicacaoDto> publicacoesDto = new ArrayList<>();

        publicacoes.forEach(p -> {
            ConteudoDaPublicacao conteudo = p.getConteudo();
            Perfil autorPrincipal = conteudo.getAutorPrincipal();

            String resumo = conteudo.getResumo();
            if( resumo.length()> 100){
                resumo= resumo.substring(0,99) + "...";
            }
            publicacoesDto.add(new PublicacaoDto(conteudo.getId(),
                    conteudo.getTipoPublicacao(),
                    new PerfilPublicacaoDto(autorPrincipal.getId(), autorPrincipal.getNome(), autorPrincipal.getVerificado(), autorPrincipal.getNomeUsuario(), autorPrincipal.getImagem())
                    , resumo
                    , conteudo.getData().toString()
                    , conteudo.getTexto()
                    , conteudo.getImagem()
                    , conteudo.getManchete()
                    , autorPrincipal.getImagem()
                    , conteudo.getVisualizacoes(),
                    conteudo.getCategoriaPublicacao(),
                    conteudo.getPropaganda()));
        });
        Usuario usuario = repositorioDeUsuario.findById(dadosDeCabecalhos.getIdUsuario()).get();


        return new RespostaFeed(publicacoesDto, pagamentosPendentes, codigoPix, tituloDoErro, descricaoDoErro, tipoErro,
                dadosDeCabecalhos.getPrimeiroAcesso(), tipoServico, usuario.getPodePublicar());
    }

    public PublicacaoDto obterPublicacao(RequisicaoParaObterPublicacao dto) {
        Publicacao publicacao = repositorioDePublicacoes.findById(dto.getIdPublicacao()).get();
        String data;
        if (publicacao.getData() == null) {
            data = LocalDateTime.now().toString();
        } else {
            data = publicacao.getData().toString();
        }
        publicacao.setVisualizacoes(publicacao.getVisualizacoes() + 1);
        repositorioDePublicacoes.save(publicacao);
        return new PublicacaoDto(publicacao.getId(),
                publicacao.getTipoPublicacao(),
                PerfilPublicacaoDto.montar(publicacao.getAutorPrincipal()),
                publicacao.getResumo(),
                data.toString(),
                publicacao.getTexto(),
                publicacao.getImagem(),
                publicacao.getManchete(),
                publicacao.getAutorPrincipal().getImagem(),
                publicacao.getVisualizacoes(),
                publicacao.getCategoriaPublicacao(),
                publicacao.getEmPropaganda()
        );

    }

    public RespostaPublicacoes buscarPublicacoesDePerfil(DadosDeCabecalhos dadosDeCabecalhos) {
        List<Publicacao> byIdPerfil = repositorioDePublicacoes.findByIdPerfil(dadosDeCabecalhos.getIdPerfilPrincipal());
        Usuario usuario = repositorioDeUsuario.findById(dadosDeCabecalhos.getIdUsuario()).get();
        List<PublicacaoDto> dto = new ArrayList<>();
        byIdPerfil.forEach(p -> dto.add(new PublicacaoDto(p.getId(), p.getTipoPublicacao(), null,
                p.getResumo(), p.getData().toString(), p.getTexto(), p.getImagem(), p.getManchete(),
                p.getAutorPrincipal().getImagem(), p.getVisualizacoes(),
                p.getCategoriaPublicacao(),
                p.getEmPropaganda())));
        return new RespostaPublicacoes(dto, null, null, null, null, null, null, usuario.getPodePublicar());

    }


    public void publicarPropaganda(ReqPublicacaoDePropaganda dto, DadosDeCabecalhos dadosDeCabecalhos) {
        /**
         private String texto;
         private TipoPublicacao tipoPublicacao;
         private String resumo;
         private byte[] imagem;
         private Integer visualizacoes;
         private Integer NaBibliotecaDePessoas;
         private Integer curtidas;
         private String manchete;
         private Long idPerfil;
         private CategoriaPublicacao categoriaPublicacao;
         private String categoriasDaPropaganda;
         private Boolean emPropaganda;

         private StatusPropaganda statusPropaganda;
         */
        Perfil perfil = repositorioDePerfil.findById(dadosDeCabecalhos.getIdPerfilPrincipal()).get();
        repositorioDePublicacoesDePropaganda.save(PublicacaoDePropaganda.builder()
                .id(null)
                .texto(dto.getTexto())
                .data(LocalDateTime.now())
                .tipoPublicacao(dto.getTipoPublicacao())
                .autorPrincipal(perfil)
                .data(LocalDateTime.now())
                .resumo(dto.getResumo())
                .imagem(dto.getImagem())
                .visualizacoes(0)
                .naBibliotecaDePessoas(0)
                .curtidas(0)
                .manchete(dto.getManchete())
                .idPerfil(perfil.getId())
                .categoriasDaPropaganda("{}")
                .propaganda(true)
                .statusPropaganda(StatusPropaganda.ATIVA)

                .build());
    }
}
