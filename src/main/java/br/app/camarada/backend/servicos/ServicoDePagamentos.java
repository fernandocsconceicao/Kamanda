package br.app.camarada.backend.servicos;


import br.app.camarada.backend.client.MercadoPagoClient;
import br.app.camarada.backend.client.WorldTimeClient;
import br.app.camarada.backend.dto.*;
import br.app.camarada.backend.dto.mercadopago.MercadoPagoPixRequest;
import br.app.camarada.backend.dto.mercadopago.MercadoPagoPixResponse;
import br.app.camarada.backend.dto.mercadopago.Payer;
import br.app.camarada.backend.entidades.*;
import br.app.camarada.backend.enums.FormaDePagamento;
import br.app.camarada.backend.enums.StatusPagamento;
import br.app.camarada.backend.enums.StatusPedido;
import br.app.camarada.backend.enums.TipoServico;
import br.app.camarada.backend.exception.ErroPadrao;
import br.app.camarada.backend.exception.ExcessaoDePagamentoCancelado;
import br.app.camarada.backend.exception.PagamentoException;
import br.app.camarada.backend.repositorios.RepositorioDeContaFinanceira;
import br.app.camarada.backend.repositorios.RepositorioDePagamento;
import br.app.camarada.backend.repositorios.RepositorioDeProdutos;
import br.app.camarada.backend.repositorios.RepositorioDeUsuario;
import br.app.camarada.backend.utilitarios.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServicoDePagamentos {


    private final MercadoPagoClient mpClient;
    private final RepositorioDePagamento pagamentoRepository;
    private final RepositorioDeContaFinanceira repositorioDeContaFinanceira;
    private final RepositorioDeUsuario repositorioDeUsuario;
    private final RepositorioDeProdutos repositorioDeProdutos;
    private final WorldTimeClient worldTimeClient;
    private final ServicoDaLoja servicoDaLoja;
    private final ServicoDaRevolucaoBrasileira servicoDaRevolucaoBrasileira;
    private final ServicoDeEstabelecimentos servicoDeEstabelecimentos;


    public Pagamento obterPagamento(Long codigoPix) {
        Pagamento pagamento = pagamentoRepository.findByPixId(codigoPix);
        return pagamento;
    }

    @Transactional
    public MercadoPagoPixResponse criarPagamentoPix(ReqCriacaoDePagamento dto, String email, String nome,
                                                    Long usuarioId, Long carrinhoId, Long regiaoId,
                                                    Long clienteId, Long idEstabelecimento) {


        if (dto.getTipoServico() == TipoServico.ENTREGA) {

            return null;
        }
        if (dto.getTipoServico() == TipoServico.ADICIONAR_SALDO) {
            MercadoPagoPixResponse retornoPagamento = mpClient.criarPagamento(MercadoPagoPixRequest.builder()
                            .transaction_amount(Double.valueOf(dto.getValor()))
                            .payment_method_id("pix")

                            .payer(
                                    Payer.builder()
                                            .email("fernando.csconceicao@outlook.com")
                                            .first_name(nome)
                                            .build()
                            )
                            .build(),
                    "Bearer APP_USR-4728572378531572-080201-0887a380581d3b8def20f0b5c13c7771-468378537"
//                    "Bearer TEST-4728572378531572-080201-2b53fbcfe6eac3583c6efd716966e89e-468378537"
            );


            BigDecimal valorEntregador = BigDecimal.valueOf(0);
            BigDecimal valorUbuntu = BigDecimal.valueOf(0);
            Pagamento pagamento = new Pagamento(null,
                    StatusPagamento.PENDENTE, retornoPagamento.transaction_amount,
                    LocalDateTime.now(), retornoPagamento.id, usuarioId, valorUbuntu, valorEntregador, TipoServico.ADICIONAR_SALDO, FormaDePagamento.PIX);

            pagamentoRepository.save(pagamento);
            return retornoPagamento;
        } else if (dto.getTipoServico() == TipoServico.COMPRA) {
            Usuario usuario = repositorioDeUsuario.findById(usuarioId).get();
            MercadoPagoPixResponse retornoPagamento = mpClient.criarPagamento(MercadoPagoPixRequest.builder()
                            .transaction_amount(usuario.getSimulacaoPrecoFinalDaSimulacao().doubleValue())
                            .payment_method_id("pix")

                            .payer(
                                    Payer.builder()
                                            .email("fernando.csconceicao@outlook.com")
                                            .first_name(nome)
                                            .build()
                            )
                            .build(),
                    "Bearer APP_USR-4728572378531572-080201-0887a380581d3b8def20f0b5c13c7771-468378537"
//                    "Bearer TEST-4728572378531572-080201-2b53fbcfe6eac3583c6efd716966e89e-468378537"
            );
            BigDecimal valorEntregador = BigDecimal.valueOf(0);
            BigDecimal valorUbuntu = BigDecimal.valueOf(0);
            Pagamento pagamento = new Pagamento(null,
                    StatusPagamento.PENDENTE, retornoPagamento.transaction_amount,
                    LocalDateTime.now(), retornoPagamento.id, usuarioId, valorUbuntu, valorEntregador, TipoServico.COMPRA, FormaDePagamento.PIX);

            usuario.setEmCompra(true);
            repositorioDeUsuario.save(usuario);
            pagamentoRepository.save(pagamento);
            return retornoPagamento;

        }

        log.info("F - Pagamento Pix pedido ");
        return null;

    }



    public MercadoPagoPixResponse verificarPagamento(Long usuarioId, String codigo) {
        try {
            MercadoPagoPixResponse mercadoPagoPixResponse = null;
            if (codigo != null) {
                mercadoPagoPixResponse = getMercadoPagoPixResponse(codigo);

            }
            List<Pagamento> pagamentos = pagamentoRepository.findByUsuarioIdOrderByHoraDoPedidoDesc(usuarioId);
            pagamentos = pagamentos.stream().filter(i -> i.getFormaDePagamento() == FormaDePagamento.PIX).collect(Collectors.toList());

            if (!pagamentos.isEmpty()) {
                Pagamento pagamento = pagamentos.get(0);
                if (pagamento.getUsuarioId().equals(usuarioId)) {
                    if (mercadoPagoPixResponse == null) {
                        if (pagamento.getFormaDePagamento() == FormaDePagamento.PIX)
                            mercadoPagoPixResponse = consultarPagamento(pagamento.getPixId().toString());
                        else
                            throw new ErroPadrao("Algo errado");
                    }
                    if (mercadoPagoPixResponse.status.equals("approved")) {
                        if (pagamento.getTipoServico() == TipoServico.ADICIONAR_SALDO) {
                            ContaFinanceira contaFinanceira = repositorioDeContaFinanceira.obterPorUsuario(usuarioId);
                            BigDecimal saldo = contaFinanceira.getSaldo();
                            contaFinanceira.setSaldo(saldo.add(BigDecimal.valueOf(pagamento.getValor())));
                            repositorioDeContaFinanceira.save(contaFinanceira);
                            System.out.println("Saldo modificado");
                        }
                        if (pagamento.getTipoServico() == TipoServico.COMPRA) {
                            Usuario usuario = repositorioDeUsuario.findById(usuarioId).get();

                            servicoDaLoja.criarPedido(usuario, StatusPedido.PEDIDO,pagamento);
                            System.out.println("Pedido Feito");
                        }
                        pagamento.setStatus(StatusPagamento.PAGO);
                        pagamentoRepository.save(pagamento);


                    }
                    if (mercadoPagoPixResponse.status.equals("cancelled") || mercadoPagoPixResponse.date_of_expiration.before(Date.from(Instant.now()))) {
                        pagamento = pagamentoRepository.findByPixId(Long.parseLong(codigo));
                        pagamento.setStatus(StatusPagamento.CANCELADO);
                        pagamentoRepository.save(pagamento);
                        throw new ExcessaoDePagamentoCancelado();
                    }
                    return mercadoPagoPixResponse;

                }
            }


        } catch (IllegalArgumentException | OptimisticLockingFailureException e) {
            String message = new StringBuilder
                    ("Erro ao criar pedido: ").append(e.getMessage())
                    .toString();
            log.error(message);
            throw new PagamentoException(message);
        } catch (NoSuchElementException e) {
            String message = new StringBuilder
                    ("Totem inválido: ").append(e.getMessage())
                    .toString();
            log.error(message);
            throw new PagamentoException(message);
        }

        throw new PagamentoException("Não foi possivel verificar seus pagamentos, cheque seus dados");
    }



    public MercadoPagoPixResponse getMercadoPagoPixResponse(String codigo) {
        MercadoPagoPixResponse mercadoPagoPixResponse = consultarPagamento(codigo);

        return mercadoPagoPixResponse;
    }

    private MercadoPagoPixResponse consultarPagamento(String codigo) {
        return mpClient.verificarPagamento(
                codigo,
                "Bearer APP_USR-4728572378531572-080201-0887a380581d3b8def20f0b5c13c7771-468378537"
//                "Bearer TEST-4728572378531572-080201-2b53fbcfe6eac3583c6efd716966e89e-468378537"
        );
    }


    public List<Pagamento> verificarPagamentosPendentes(Long usuario) {
        List<Pagamento> pagamentos = pagamentoRepository.findByUsuarioIdOrderByHoraDoPedidoDesc(usuario);
        List<Pagamento> pagamentosPendentes = pagamentos.stream().filter(p -> p.getStatus().equals(StatusPagamento.PENDENTE)).collect(Collectors.toUnmodifiableList());
        if (pagamentosPendentes.size() > 0) {
            return pagamentosPendentes;
        }
        return null;
    }

    public void cancelarPagamentoPendente(Long usuario) {
        List<Pagamento> pagamentos = pagamentoRepository.findByUsuarioIdOrderByHoraDoPedidoDesc(usuario);
        List<Pagamento> pagamentosPendentes = pagamentos.stream().filter(p -> p.getStatus().equals(StatusPagamento.PENDENTE)).collect(Collectors.toUnmodifiableList());
        Pagamento pagamento = pagamentosPendentes.get(0);
        pagamento.setStatus(StatusPagamento.CANCELADO);
        pagamentoRepository.save(pagamento);
    }


    public BigDecimal obterSaldo(Long idContaFinanceira) {
        ContaFinanceira contaFinanceira = repositorioDeContaFinanceira.findById(idContaFinanceira).get();
        return contaFinanceira.getSaldo();
    }

    @Transactional
    public void pagarComSaldo(Long idContaFinanceira, RequisicaoPagarComSaldo dto, Long idEstabelecimento, Long usuarioId) throws ClassNotFoundException, InterruptedException {
        ContaFinanceira contaFinanceira = repositorioDeContaFinanceira.findById(idContaFinanceira).get();
        RespostaHoraAtualWorldTime horaAtual = worldTimeClient.buscarHora();
        BigDecimal saldo = contaFinanceira.getSaldo();
        Usuario usuario = repositorioDeUsuario.findById(usuarioId).get();
        if (Double.valueOf(saldo.toString()) < Double.valueOf(dto.getValor().doubleValue())) {
            throw new ClassNotFoundException();
        } else {
            contaFinanceira.setSaldo(contaFinanceira.getSaldo().subtract(dto.getValor()));
        }
        if (dto.getTipoServico().equals(TipoServico.ENTREGA)) {
            System.out.println("Requisição de entrega incabida");

        } else if (dto.getTipoServico().equals(TipoServico.COMPRA)) {
            Pagamento pagamento = new Pagamento(
                    null,
                    StatusPagamento.PAGO,
                    usuario.getSimulacaoPrecoFinalDaSimulacao().doubleValue(),
                    LocalDateTime.now(),
                    null,
                    usuarioId,
                    null,
                    null,
                    dto.getTipoServico(),
                    FormaDePagamento.SALDO);


            servicoDaLoja.criarPedido(usuario,StatusPedido.PEDIDO,pagamentoRepository.save(pagamento));

        }


    }

    public void criarContaFinanceira(Long idUsuario) {
        RespostaHoraAtualWorldTime respostaHoraAtualWorldTime = worldTimeClient.buscarHora();
        ContaFinanceira conta = repositorioDeContaFinanceira.save(
                new ContaFinanceira(null, idUsuario, BigDecimal.ZERO, "",
                        respostaHoraAtualWorldTime.getDatetime().toLocalDateTime())
        );
        Usuario usuario = repositorioDeUsuario.findById(idUsuario).get();
        usuario.setContaFinanceira(conta);
        repositorioDeUsuario.save(usuario);

    }
}
