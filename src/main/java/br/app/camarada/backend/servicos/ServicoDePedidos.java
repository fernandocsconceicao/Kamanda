package br.app.camarada.backend.servicos;

import br.app.camarada.backend.dto.*;
import br.app.camarada.backend.entidades.*;
import br.app.camarada.backend.enums.PlanoEstabelecimento;
import br.app.camarada.backend.enums.StatusEntregaEmFila;
import br.app.camarada.backend.enums.StatusEntregador;
import br.app.camarada.backend.enums.StatusPedido;
import br.app.camarada.backend.exception.ErroPadrao;
import br.app.camarada.backend.repositorios.RepositorioDePedidos;
import br.app.camarada.backend.repositorios.RepositorioDeUsuario;
import br.app.camarada.backend.utilitarios.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServicoDePedidos {

    private final RepositorioDePedidos repositorioDePedidos;
    //    private RepositorioProdutosDePedidos repositorioDeProdutosDePedidos;
    private final ServicoDaLoja servicoDaLoja;
    private final ServicoDeRegiao servicoDeRegiao;
    private final ServicoDeEstabelecimentos servicoDeEstabelecimentos;
    private final ServicoDaRevolucaoBrasileira servicoDaRevolucaoBrasileira;
    private final ServicoFinanceiro servicoFinanceiro;
    private final ServicoDeEndereco servicoDeEndereco;

}


//    public ResTelaViagens obterTelaDePedidos(Long idRegiao, Long idEntregador) {
//
//        List<Pedido> pedidos = repositorioDePedidos.findByRegiaoId(idRegiao);
//
//
//        return ResTelaViagens.build(pedidos.stream().filter(pedido -> pedido.getStatus().equals(StatusPedido.AGUARDANDO_ENTREGA)
//                && pedido.getOferecidoParaEntregador().equals(idEntregador)).collect(Collectors.toUnmodifiableList()));
//
//    }



//    public void avaliarPedidos(AvaliacaoDePedido dto) {
//        for (ReqAvaliacaoDeProdutoDto avaliacaoProduto : dto.getAvaliacoesProduto()) {
//            avaliarProduto(avaliacaoProduto.getIdProduto(), avaliacaoProduto);
//        }
//        Pedido pedido = repositorioDePedidos.findById(dto.getIdPedido()).get();
//        pedido.setStatus(
//                StatusPedido.AVALIADO
//        );
//        repositorioDePedidos.save(pedido);
//
//    }

//    private void avaliarProduto(Long idProduto, ReqAvaliacaoDeProdutoDto avaliacao) {
//        servicoDeProdutos.avaliar(idProduto, avaliacao.getNota(), avaliacao.getObservacao());
//    }

//    public EntregaEmFila obterTelaDePedidoParaEntregador(Long idEntregador) {
//        Entregador entregador = servicoDaRevolucaoBrasileira.obterEntregador(idEntregador);
//
//        Optional<EntregaEmFila> byId = servicoDaRevolucaoBrasileira.obterEntrega(entregador.getEntregaEmQueSeEstaTrabalhando());
//        if (byId.isPresent()) {
//            return byId.get();
//        } else {
//            return null;
//        }
//    }

//    public void forcarFinalizacaoDeViagemDoEntregador(Long idEntregador) {
//
//        Entregador entregador = servicoDaRevolucaoBrasileira.obterEntregador(idEntregador);
//        entregador.setStatus(StatusEntregador.TRABALHANDO);
//        servicoDaRevolucaoBrasileira.salvarEntregador(entregador);
//        Entregador teste = servicoDaRevolucaoBrasileira.obterEntregador(idEntregador);
//        System.out.println(teste.getStatus());
//
//        EntregaEmFila entregaEmFila = servicoDaRevolucaoBrasileira.obterEntrega((entregador.getEntregaEmQueSeEstaTrabalhando())).get();
//        entregaEmFila.setStatusEntregaEmFila(StatusEntregaEmFila.CONCLUIDA);
//        servicoDaRevolucaoBrasileira.salvarEntregaEmFila(entregaEmFila);
//    }


