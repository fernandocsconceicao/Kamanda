
package br.app.camarada.backend.servicos;

import br.app.camarada.backend.client.WorldTimeClient;
import br.app.camarada.backend.dto.*;
import br.app.camarada.backend.dto.mensagem.Mensagem;
import br.app.camarada.backend.entidades.*;
import br.app.camarada.backend.enums.*;
import br.app.camarada.backend.repositorios.*;
import br.app.camarada.backend.utilitarios.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.app.camarada.backend.enums.StatusEntregaEmFila.ATENDIDO;
import static br.app.camarada.backend.enums.StatusEntregaEmFila.DISPONIVEL;


@Service
@AllArgsConstructor
@Slf4j
public class ServicoDaRevolucaoBrasileira {

    private RepositorioDaRevolucaoBrasileira repositorioDeRevolucionario;
    private RepositorioDeUsuario repositorioDeUsuario;
    private RepositorioFilaDeEntregas repositorioFilaDeEntregas;
    private RepositorioDePedidos repositorioDePedidos; //TODO: Gambiarra para desviar de ciclo
    private WorldTimeClient worldTimeClient;
    private EstabelecimentoEnderecoRepository estabelecimentoEnderecoRepository;
//    private ServicoDePedidos servicoDePedidos;




    public Entregador criarEntregador(CriacaoDoEntregador propriedades) {
        return repositorioDeRevolucionario.save(Entregador.build(propriedades));

    }

    @Scheduled(fixedRate = 10 * 1000)
    @Transactional
    public synchronized void girarFila() throws JsonProcessingException, InterruptedException {

        List<EntregaEmFila> entregasDiponiveisEmFilas = repositorioFilaDeEntregas.findAll();
        entregasDiponiveisEmFilas = entregasDiponiveisEmFilas.stream().filter
                        (entregaEmFila -> entregaEmFila.getStatusEntregaEmFila().equals(DISPONIVEL))
                .collect(Collectors.toList());
        if (!entregasDiponiveisEmFilas.isEmpty()) {


            EntregaEmFila entregaEmFila = entregasDiponiveisEmFilas.get(0);

//            Pedido pedido = repositorioDePedidos.findById(entregaEmFila.getIdPedido()).get();

            Entregador entrgadorAcionado = acionarEntregador(
                    entregaEmFila
                    , true
            );

//            if (entrgadorAcionado != null) {
//                entregaEmFila.setStatusEntregaEmFila(EM_NEGOCIACAO);
//            }
//            repositorioFilaDeEntregas.save(entregaEmFila);
        } else {

        }
    }

    public Entregador acionarEntregador(EntregaEmFila entregaEmFila, Boolean jaEmFila) {

        Entregador entregador = null;
        List<Entregador> entregadores = repositorioDeRevolucionario.obterEntregadorDisponivel(); // Adiocionar alguma condição para ordenar quem será chamado primeiro

        if (entregadores.isEmpty()) {

            if (!jaEmFila) {
                BigDecimal valorEstabelecimento = null;
                adicionarPedidoAFila(
                        entregaEmFila.getIdPedido(), null, null,
                        false, null,
                        new DadosFinanceirosEntrega(
                                entregaEmFila.getValorEntrega(),
                                valorEstabelecimento,
                                entregaEmFila.getValorTotal(),
                                entregaEmFila.getValorUbuntu()));
            }


            return null;
        } else {
            entregador = entregadores.get(0);
            entregador.setStatus(StatusEntregador.AVALIANDO);
            entregador.setEntregaEmQueSeEstaTrabalhando(entregaEmFila.getId());

            entregador = repositorioDeRevolucionario.save(entregador);

            repositorioDeRevolucionario.findById(entregador.getId());

        }

        return entregador;
    }


    public void adicionarPedidoAFila(Long pedidoId, Long usuarioId, Long idEstabelecimento, Boolean planoA,
                                     SolicitacaoDeEntrega dto,
                                     DadosFinanceirosEntrega dadosFinanceirosEntrega
    ) {

        String enderecoInicial = "";
        String enderecoFinal = "";
        String nomeEstabelecimento = "Indefinido";
        String nomeCliente = "";
        BigDecimal valorEntrega;
        BigDecimal valorUbuntu;

        Optional<Estabelecimento_Endereco> estabelecimentoOpt = estabelecimentoEnderecoRepository.findById(idEstabelecimento);
        if (pedidoId != null) {

            Pedido pedido = repositorioDePedidos.findById(pedidoId).get();
            valorEntrega = pedido.getValorCorrida();
            valorUbuntu = pedido.getValorKamanda();
            enderecoInicial = pedido.getEnderecoCliente();
            enderecoFinal = pedido.getEnderecoEstabelecimento();
            nomeCliente = pedido.getNomeCliente();

        } else {

            nomeCliente = dto.getNomeCliente();
            nomeEstabelecimento = estabelecimentoOpt.get().getName();
            enderecoInicial = estabelecimentoOpt.get().getEndereco().getEnderecoCompleto();
            enderecoFinal = dto.getEnderecoFinal();
            valorEntrega = estabelecimentoOpt.get().getValorTotalSimulacaoEntrega();
            valorUbuntu = estabelecimentoOpt.get().getValorUbuntuSimulacaoEntrega();

        }
//        if (estabelecimentoOpt.isPresent()) {
//
//            if (dto != null && planoA) {
//
//
//            }
//        }
        EntregaEmFila entregaEmFila = new EntregaEmFila(
                null,
                DISPONIVEL,
                pedidoId,
                null,
                null,
                worldTimeClient.buscarHora().getDatetime().toLocalDateTime(),
                null,
                idEstabelecimento,
                planoA,
                usuarioId,
                enderecoInicial,
                enderecoFinal,
                nomeCliente,
                nomeEstabelecimento,
                valorEntrega,
                valorUbuntu,
                dadosFinanceirosEntrega.getValorTotal()


        );
        repositorioFilaDeEntregas.save(entregaEmFila);
    }

    public void desconectarEntregador(Long entregadorId) {

        Optional<Entregador> entregadorOpt = repositorioDeRevolucionario.findById(entregadorId);
        if (entregadorOpt.isPresent()) {
            Entregador value = entregadorOpt.get();
            value.setStatus(StatusEntregador.OFFLINE);
            repositorioDeRevolucionario.save(value);
        }
    }

    private List<Entregador> obterEntregadoresConectados() {
        repositorioDeRevolucionario.obterEntregadorDisponivel();
        return null;
    }

    public boolean iniciarTrabalho(Long idEntregador) {
        Entregador entregador = repositorioDeRevolucionario.findById(idEntregador).get();
        entregador.setStatus(StatusEntregador.TRABALHANDO);

        repositorioDeRevolucionario.save(entregador);
        return true;
    }

    public StatusEntregador finalizarTrabalho(Entregador entregador, WebSocketSession session) throws IOException {
        entregador.setStatus(StatusEntregador.ONLINE);
        salvarSessaoId(entregador, session.getId());
        repositorioDeRevolucionario.save(entregador);
        ObjectMapper objectMapper = new ObjectMapper();
        Mensagem mensagem = new Mensagem(
                TipoMensagem.FINALIZAR_TRABALHO,
                null,
                null,
                null,
                null,
                null,
                entregador.getId().toString(),
                StatusEntregador.ONLINE.toString(),
                Remetente.BACKEND,
                null,
                null,
                null,
                null,
                null


        );

        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(mensagem)));
        return StatusEntregador.ONLINE;
    }

    public StatusEntregador finalizarTrabalho(Long idEntregador) {
        Entregador entregador = obterEntregador(idEntregador);
        entregador.setStatus(StatusEntregador.ONLINE);
        repositorioDeRevolucionario.save(entregador);
        return StatusEntregador.ONLINE;
    }

    private void salvarSessaoId(Entregador entregador, String sessaoId) {
        Usuario usuario = repositorioDeUsuario.findById(entregador.getUsuarioId()).get();
        usuario.setIdDaSessaoWs(sessaoId);
        repositorioDeUsuario.save(usuario);
    }


    public Entregador obterEntregador(Long idEntregador) {
        Optional<Entregador> optionalEntregador = repositorioDeRevolucionario.findById(idEntregador);
        if (optionalEntregador.isPresent()) {
            return optionalEntregador.get();
        } else {
            return null;
        }
    }

    public StatusEntregador entregarPedidoParaEntregador(Entregador entregador,
                                                         Long idPedido,
                                                         WebSocketSession session) {
        return null;
    }

    public void salvarEntregador(Entregador entregador) {
        repositorioDeRevolucionario.save(entregador);
    }

    public void atualizarStatusDePedidoEmFila(Pedido pedido, StatusEntregaEmFila status) {
        EntregaEmFila entregaEmFila = repositorioFilaDeEntregas.obterPorIdPedido(pedido.getId());
        entregaEmFila.setStatusEntregaEmFila(status);
        repositorioFilaDeEntregas.save(entregaEmFila);
    }

    public void atualizarStatusDePedidoEmFila(Long idEntrega, StatusEntregaEmFila status) {
        EntregaEmFila entregaEmFila = repositorioFilaDeEntregas.findById(idEntrega).get();
        entregaEmFila.setStatusEntregaEmFila(status);
        repositorioFilaDeEntregas.save(entregaEmFila);
    }

    public Optional<EntregaEmFila> obterEntrega(Long id) {
        return repositorioFilaDeEntregas.findById(id);
    }

    public void salvarEntregaEmFila(EntregaEmFila entregaEmFila) {
        repositorioFilaDeEntregas.save(entregaEmFila);
    }

    public List<EntregaEmFila> obterEntregasDeEstabelecimento(Long idEstabelecimento) {
        return repositorioFilaDeEntregas.obterPorEstabelecimento(idEstabelecimento);
    }

    public void excluirEntregador(Long id) {
        Entregador entregador = repositorioDeRevolucionario.findByUsuarioId(id).get();
        entregador.setStatus(StatusEntregador.EXCLUIDO);
        entregador.setNome("");
        entregador.setCelular("");
        RespostaHoraAtualWorldTime worldTime = worldTimeClient.buscarHora();
        entregador.setDataExclusao(worldTime.getDatetime().toLocalDate());
        repositorioDeRevolucionario.save(entregador);
    }

    public void responderCorrida(Long id, Boolean resposta) {
        log.info("I - Resposta Corrida");
        Entregador entregador = obterEntregador(id);
        if (entregador != null) {
            Optional<EntregaEmFila> entregaEmFilaOpt = obterEntrega(entregador.getEntregaEmQueSeEstaTrabalhando());
            if (entregaEmFilaOpt.isPresent()) {

                if (entregaEmFilaOpt.get().getPlanoA()) {
                    EntregaEmFila entregaEmFila = entregaEmFilaOpt.get();
                    lidarComRespostaDeCorridaSemPedido(entregaEmFila.getId(), resposta, entregador);
                } else {
                    Pedido pedido = repositorioDePedidos.findById(entregaEmFilaOpt.get().getIdPedido()).get();
                    lidarComRespostaDeCorrida(resposta, entregador, pedido, id);
                }
            }


        } else {
            throw new NullPointerException("Id inválido");
        }
    }

    private void lidarComRespostaDeCorrida(Boolean resposta, Entregador entregador, Pedido pedido, Long id) {
        if (resposta) {
            entregador.setStatus(StatusEntregador.EM_VIAGEM);
            pedido.setStatus(StatusPedido.ENTREGANDO);
            repositorioDePedidos.save(pedido);
            atualizarStatusDePedidoEmFila(
                    pedido,
                    ATENDIDO);
            salvarEntregador(entregador);
        } else {
            atualizarStatusDePedidoEmFila(
                    pedido,
                    StatusEntregaEmFila.DISPONIVEL);
            entregador.setStatus(StatusEntregador.TRABALHANDO);
            entregador.setEntregaEmQueSeEstaTrabalhando(null);
            salvarEntregador(entregador);
        }

        // Para tentar forçar com que as alterações de status sejam feitas na hora
        Entregador entregador1 = obterEntregador(id);
        entregador1.setNome(entregador.getNome());
        salvarEntregador(entregador1);
        log.info("F - Resposta Corrida");
    }

    private void lidarComRespostaDeCorridaSemPedido(Long idEntregaEmFila, Boolean resposta, Entregador entregador) {
        if (resposta) {
            atualizarStatusDePedidoEmFila(idEntregaEmFila, ATENDIDO);
            entregador.setStatus(StatusEntregador.EM_VIAGEM);
        } else {
            atualizarStatusDePedidoEmFila(idEntregaEmFila, DISPONIVEL);
            entregador.setStatus(StatusEntregador.TRABALHANDO);
            entregador.setEntregaEmQueSeEstaTrabalhando(null);
        }
        salvarEntregador(entregador);
        log.info("F - Resposta Corrida");
    }

    public ResOfertaCorrida obteroferta(Long idEntregador) {
        Entregador entregador = obterEntregador(idEntregador);
        EntregaEmFila entregaEmFila = repositorioFilaDeEntregas.findById(entregador.getEntregaEmQueSeEstaTrabalhando()).get();

        return new ResOfertaCorrida(
                entregaEmFila.getEnderecoInicial(),
                entregaEmFila.getEnderecoFinal(),
                StringUtils.formatPrice(entregaEmFila.getValorEntrega())
        );

    }
}
