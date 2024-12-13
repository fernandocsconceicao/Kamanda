package br.app.camarada.backend.servicos;

import br.app.camarada.backend.dto.DadosDeCabecalhos;
import br.app.camarada.backend.dto.PerfilDto;
import br.app.camarada.backend.dto.PublicacaoDto;
import br.app.camarada.backend.dto.RespostaFeed;
import br.app.camarada.backend.dto.mercadopago.MercadoPagoPixResponse;
import br.app.camarada.backend.dto.publicacao.req.RequisicaoParaObterPublicacao;
import br.app.camarada.backend.dto.publicacao.res.RespostaPublicacoes;
import br.app.camarada.backend.entidades.Pagamento;
import br.app.camarada.backend.entidades.Publicacao;
import br.app.camarada.backend.enums.FormaDePagamento;
import br.app.camarada.backend.enums.TextosBundle;
import br.app.camarada.backend.enums.TipoErro;
import br.app.camarada.backend.enums.TipoServico;
import br.app.camarada.backend.exception.ExcessaoDePagamentoCancelado;
import br.app.camarada.backend.repositorios.RepositorioDePublicacoes;
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
    private ServicoDePagamentos servicoDePagamentos;

    public RespostaFeed buscarPublicacoes(DadosDeCabecalhos dadosDeCabecalhos) {
        List<Pagamento> pagamentos = servicoDePagamentos.verificarPagamentosPendentes(dadosDeCabecalhos.getIdUsuario());
        boolean pagamentosPendentes = false;
        String codigoPix = null;
        String tituloDoErro = null;
        String descricaoDoErro = null;
        TipoErro tipoErro = null;
        TipoServico tipoServico = null;
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
        List<Publicacao> publicacoes = repositorioDePublicacoes.findAll();

        return new RespostaFeed(publicacoes, pagamentosPendentes, codigoPix, tituloDoErro, descricaoDoErro, tipoErro, dadosDeCabecalhos.getPrimeiroAcesso(), tipoServico);
    }

    public PublicacaoDto obterPublicacao(RequisicaoParaObterPublicacao dto) {
        System.out.println(dto.getIdPublicacao());
        Publicacao publicacao = repositorioDePublicacoes.findById(dto.getIdPublicacao()).get();
        System.out.println("fdsfsd");
        String data;
        if (publicacao.getData() == null) {
            data = LocalDateTime.now().toString();
        } else {
            data = publicacao.getData().toString();
        }
        return new PublicacaoDto(publicacao.getId(),
                publicacao.getTipoPublicacao(),
                PerfilDto.montar(publicacao.getAutorPrincipal()),
                publicacao.getResumo(),
                data.toString(),
                publicacao.getTexto(),
                publicacao.getImagem(),
                publicacao.getManchete(),
                publicacao.getAutorPrincipal().getImagem()
        );

    }

    public RespostaPublicacoes buscarPublicacoesDePerfil(DadosDeCabecalhos dadosDeCabecalhos) {
        List<Publicacao> byIdPerfil = repositorioDePublicacoes.findByIdPerfil(dadosDeCabecalhos.getIdPerfilPrincipal());
        List<PublicacaoDto> dto= new ArrayList<>();
        byIdPerfil.forEach(p-> dto.add(new PublicacaoDto(p.getId(), p.getTipoPublicacao(),null,
                p.getResumo(),p.getData().toString(),p.getTexto(),p.getImagem(),p.getManchete(),
                p.getAutorPrincipal().getImagem())));
        return new RespostaPublicacoes(dto,null,null,null,null,null,null);

    }
}
