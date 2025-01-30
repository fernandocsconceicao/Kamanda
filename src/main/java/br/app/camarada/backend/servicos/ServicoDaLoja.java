package br.app.camarada.backend.servicos;

import br.app.camarada.backend.client.GoogleMapsClient;
import br.app.camarada.backend.dto.Properties;
import br.app.camarada.backend.dto.*;
import br.app.camarada.backend.entidades.*;
import br.app.camarada.backend.enums.StatusPedido;
import br.app.camarada.backend.exception.ErroPadrao;
import br.app.camarada.backend.repositorios.*;
import br.app.camarada.backend.utilitarios.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
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
@Slf4j
@AllArgsConstructor
public class ServicoDaLoja {

    private RepositorioDeUsuario repositorioDeUsuario;
    private RepositorioDeProdutos repositorioDeProdutos;
    private RepositorioDeEstabelecimentos repositorioDeEstabelecimentos;
    private GoogleMapsClient googleMapsClient;
    private ServicoDoGoogleMaps servicoDoGoogleMaps;
    private RepositorioDeEnderecos repositorioDeEnderecos;
    private RepositorioDePedidos repositorioDePedidos;


    public ProductScreen obterTelaDeProduto(Long id) {
        Produto produto = repositorioDeProdutos.findById(id).get();
        return ProductScreen.build(produto, getProductProperties(produto), produto.getPrecoVitrine());
    }

    @Transactional
    public void adicionarProduto(ReqCriacaoDeProduto dto, DadosDeCabecalhos dadosDeCabecalhos) {
        try {
            dadosDeCabecalhos.getIdEstabecimento();
            Optional<Estabelecimento> establishment = repositorioDeEstabelecimentos.findById(dadosDeCabecalhos.getIdEstabecimento());
            if (establishment.isEmpty()) throw new ErroPadrao("Estabelecimento não encontrado");
            Produto produto = Produto.build(dto, establishment.get());
            repositorioDeProdutos.save(produto);

        } catch (IllegalArgumentException | OptimisticLockingFailureException e) {
            String message = new StringBuilder
                    ("Erro ao criar produto: ").append(e.getMessage())
                    .toString();
            log.error(message);
            throw new ErroPadrao(message);
        }

    }



    public TelaCarrinho obterCarrinho(DadosDeCabecalhos dadosDeCabecalhos) throws JsonProcessingException {
        Usuario usuario = repositorioDeUsuario.findById(dadosDeCabecalhos.getIdUsuario()).get();

        String carrinho = usuario.getCarrinho();
        final BigDecimal[] valorTotal = {BigDecimal.ZERO};

        ObjectMapper objectMapper = new ObjectMapper();
        List<ItemCarrinho> carrinhoObj;
        if (usuario.getCarrinho() == null || usuario.getCarrinho().equals("{}"))
            carrinhoObj = new ArrayList<>();
        else {
            carrinhoObj = objectMapper.readValue(carrinho, new TypeReference<>() {
            });
        }

        carrinhoObj.forEach(itemCarrinho -> {
            valorTotal[0] = valorTotal[0].add(itemCarrinho.getPreco());
        });
        List<ItemCarrinhoComImagem> carrinhoComImagens = new ArrayList<>();
        carrinhoObj.forEach(itemCarrinho -> {
            Produto produto = repositorioDeProdutos.findById(itemCarrinho.getId()).get();
            carrinhoComImagens.add(new ItemCarrinhoComImagem(
                    itemCarrinho.getId(),
                    itemCarrinho.getTitulo(),
                    itemCarrinho.getQuantidade(),
                    itemCarrinho.getPreco(),
                    produto.getImagem()
            ));
        });


        return new TelaCarrinho(carrinhoComImagens, StringUtils.formatPrice(valorTotal[0]),usuario.getPrimeiraCompra());
    }

    public TelaVitrine obterVitrine(DadosDeCabecalhos dadosDeCabecalhos) {

        List<Produto> produtos = repositorioDeProdutos.buscarPorExibicaoEAvaliacao(10);
        List<ProdutoDto> produtoDtos = new ArrayList<>();
        produtos.forEach(p -> {
            if (p.getPrecoVitrine() == null) {
                p.setPrecoVitrine(p.getPreco().multiply(BigDecimal.valueOf(1.20)));
                repositorioDeProdutos.save(p);
            }
            produtoDtos.add(new ProdutoDto(p.getId(), p.getNome(), p.getImagem(), StringUtils.formatPrice(p.getPrecoVitrine())));

        });

        produtos.forEach(p -> {
            p.setDataExibicao(LocalDateTime.now());
        });
//fsdfs
        repositorioDeProdutos.saveAll(produtos);
        List<Estabelecimento> bestRatedEstablishment = repositorioDeEstabelecimentos.findBestRatedEstablishment();
        List<VitrineEstabelecimentoDto> vitrineEstabelecimento = new ArrayList<>();
        bestRatedEstablishment.forEach(e -> {
            List<Produto> produtosDoEstabelecimento = repositorioDeProdutos.findByEstablishmentId(e.getId());
            ArrayList<ProdutoDto> produtosDeEstabelecimentosDto = new ArrayList<>();

            produtosDoEstabelecimento.forEach(p -> produtosDeEstabelecimentosDto.add (new ProdutoDto(
                    p.getId(),p.getNome(),p.getImagem(),StringUtils.formatPrice(p.getPrecoVitrine())
                    )
            ));
            vitrineEstabelecimento.add( new VitrineEstabelecimentoDto(e.getId(), e.getName(),e.getLogo(),produtosDeEstabelecimentosDto ));
        });

        return new TelaVitrine(produtoDtos,vitrineEstabelecimento,dadosDeCabecalhos.getPrimeiraCompra());
    }

    public void adicionarAoCarrinho(AdicionamentoDeProdutoAoCarrinho dto, DadosDeCabecalhos dadosDeCabecalhos) {
        Usuario usuario = repositorioDeUsuario.findById(dadosDeCabecalhos.getIdUsuario()).get();
        //Para cada item no carrinho, se processará sua quantidade

        dto.getProdutos().forEach(itemCarrinho -> {
            Optional<Produto> opt = repositorioDeProdutos.findById(itemCarrinho.getId());
            if (opt.isPresent()) {
                Produto produto = opt.get();
                String carrinhoJson = usuario.getCarrinho();

                ObjectMapper objectMapper = new ObjectMapper();
                List<ItemCarrinho> carrinhoObjeto;
                final BigDecimal[] valorTotal = {BigDecimal.ZERO};

                if (usuario.getCarrinho() == null || usuario.getCarrinho().equals("{}"))
                    carrinhoObjeto = new ArrayList<>();
                else {
                    try {
                        carrinhoObjeto = objectMapper.readValue(carrinhoJson, new TypeReference<>() {
                        });
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
                AtomicReference<Boolean> itemAdicionado = new AtomicReference<>(false);
                carrinhoObjeto.forEach(itemCarrinhoObjeto -> {
                    if (itemCarrinhoObjeto.getId() == itemCarrinho.getId()) {

                        valorTotal[0] = valorTotal[0].add(itemCarrinhoObjeto.getPreco().multiply(BigDecimal.valueOf(itemCarrinhoObjeto.getQuantidade())));
                        itemCarrinhoObjeto.setQuantidade(itemCarrinho.getQuantidade() + itemCarrinhoObjeto.getQuantidade());
                        if (itemCarrinhoObjeto.getQuantidade() < 1) {
                            carrinhoObjeto.remove(carrinhoObjeto.indexOf(itemCarrinhoObjeto));
                            if (carrinhoObjeto.size() == 0) {
                                String json = null;
                                try {
                                    json = objectMapper.writeValueAsString(carrinhoObjeto);
                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException(e);
                                }
                                usuario.setCarrinho(json);
                                repositorioDeUsuario.save(usuario);
                                return;
                            }
                        }
                        itemAdicionado.set(true);
                    }
                });
                if (!itemAdicionado.get()) {
                    repositorioDeEstabelecimentos.findById(produto.getEstabelecimentoId());
                    carrinhoObjeto.add(new ItemCarrinho(produto.getId(), produto.getNome(),
                            itemCarrinho.getQuantidade(), produto.getPrecoVitrine(), produto.getEstabelecimentoId()));
                }

                String json = null;
                try {
                    json = objectMapper.writeValueAsString(carrinhoObjeto);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                usuario.setCarrinho(json);
                repositorioDeUsuario.save(usuario);

            }
        });

    }


    public List<Properties> getProductProperties(Produto produto) {
        List<Properties> list = new ArrayList<>();


        if (produto.getAvaliacao() != null) {
            list.add(new Properties("Avaliação", produto.getAvaliacao().toString(), null, null));
        } else {
            list.add(new Properties("Avaliação", "Ainda não avaliado", null, null));
        }


        return list;
    }

    @Transactional
    public List<Pedido> criarPedido(Usuario usuario, StatusPedido statusIniciadoDoPedido, Pagamento pagamento) {
        try {
            List<ItemCarrinho> itensDoCarrinho = StringUtils.transformarCarrinhoEmObj(usuario.getCarrinho());
            if (itensDoCarrinho.size() == 0) throw new ErroPadrao("Não existem produtos no carrinho");
            ArrayList<Pedido> pedidos = new ArrayList<>();

            String titulo = itensDoCarrinho.get(0).getTitulo();
            if (itensDoCarrinho.size() > 1) {
                titulo += " e mais " + (itensDoCarrinho.size() - 1) + " ";
                if (itensDoCarrinho.size() - 1 > 1) {
                    titulo += "itens";
                } else {
                    titulo += "item";

                }
            }
            String finalTitulo = titulo;

            Set<Long> listaDeIdsDeEstabelecimento = new HashSet<>();

            itensDoCarrinho.forEach(itemCarrinho -> listaDeIdsDeEstabelecimento.add(itemCarrinho.getIdEstabelecimento()));
            AtomicReference<BigDecimal> frete = new AtomicReference<>(new BigDecimal(0));

            listaDeIdsDeEstabelecimento.forEach(idDeEstabelecimento -> {
                        AtomicReference<BigDecimal> valorFinal = new AtomicReference<>(new BigDecimal(0));
                        //Separa os produtos do carrinho por id de estabelecimento, em ultima instancia, estes são os produtos de um pedido
                        // para este estabelecimento de id == idDeEstabelecimento
                        AtomicReference<BigDecimal> valorEstabelecimento = new AtomicReference<>(BigDecimal.ZERO);
                        List<ItemCarrinho> ItensEmPedidoParaEsteEstabelecimento = itensDoCarrinho.stream()
                                .filter(itemCarrinho -> itemCarrinho.getIdEstabelecimento().equals(idDeEstabelecimento))
                                .collect(Collectors.toUnmodifiableList());

                        AtomicReference<BigDecimal> valorTotalDoPedido = new AtomicReference<>(new BigDecimal(0));

                        //Calcular o preço total do pedido e adiciona ao valor final
                        List<ProdutoDePedido> produtosDePedido = new ArrayList<>();

                        ItensEmPedidoParaEsteEstabelecimento.forEach(
                                produtoDoPedido -> {
                                    Produto produto = repositorioDeProdutos.findById(produtoDoPedido.getId()).get();
                                    BigDecimal price = produtoDoPedido.getPreco().multiply(new BigDecimal(produtoDoPedido.getQuantidade()));
                                    valorFinal.set(valorTotalDoPedido.get().add(price));
                                    valorTotalDoPedido.set(valorTotalDoPedido.get().add(price));
                                    produtosDePedido.add(ProdutoDePedido.build(produtoDoPedido, produto));
                                    valorEstabelecimento.set(valorEstabelecimento.get().multiply(BigDecimal.valueOf(produtoDoPedido.getQuantidade())));
                                });

                        Estabelecimento estabelecimento = repositorioDeEstabelecimentos.findById(idDeEstabelecimento).get();

                        // Transformar em JSON
                        String json = null;

                        ObjectMapper objectMapper = new ObjectMapper();

                        try {
                            json = objectMapper.writeValueAsString(produtosDePedido);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        SimulacaoEntregaDto simulacaoEntregaDto;
                        Endereco endereco = repositorioDeEnderecos.findById(usuario.getEnderecoId()).get();
                        try {
                            simulacaoEntregaDto = calcularFrete(frete, endereco, estabelecimento);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                        Pedido pedido = null;
                        pedido = repositorioDePedidos.save(new Pedido(
                                        null,
                                        statusIniciadoDoPedido,
                                        usuario.getId(),
                                        usuario.getCpf(),
                                        usuario.getNome(),
                                        LocalDateTime.now(),
                                        null,
                                        estabelecimento.getId(),
                                        finalTitulo,
                                        usuario.getTelefone(),
                                        valorFinal.get(),
                                        null,
                                        finalTitulo,
                                        null,
                                        estabelecimento.getEndereco().getEnderecoCompleto(),
                                        endereco.getEnderecoCompleto(),
                                        null,
                                        null,
                                        simulacaoEntregaDto.getPrecoEntrega(),
                                        pagamento,
                                        json,
                                        produtosDePedido.get(0).getMiniatura(),
                                        valorEstabelecimento.get(),
                                        null

                                )
                        );
                        pedidos.add(pedido);
                    }
            );
            return pedidos;


        } catch (IllegalArgumentException |
                 OptimisticLockingFailureException e) {
            String message = new StringBuilder
                    ("Erro ao criar pedido: ").append(e.getMessage())
                    .toString();
            log.error(message);
            throw new ErroPadrao(message);
        } catch (
                NoSuchElementException e) {
            String message = new StringBuilder
                    ("Totem inválido: ").append(e.getMessage())
                    .toString();
            log.error(message);
            throw new ErroPadrao(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public TelaEntregaCliente obterTelaDeEntregaParaCliente(DadosDeCabecalhos dadosDeCabecalhos) {
        Usuario usuario = repositorioDeUsuario.findById(dadosDeCabecalhos.getIdUsuario()).get();
        Endereco enderecoUsuario = repositorioDeEnderecos.findById(usuario.getEnderecoId()).get();
        Endereco enderecoCliente = enderecoUsuario;

        final AtomicReference<BigDecimal>[] frete = new AtomicReference[]{new AtomicReference<>(new BigDecimal(0))};

        //lista de estabelecimentos do carrinho
        String carrinhoJson = usuario.getCarrinho();

        ObjectMapper objectMapper = new ObjectMapper();
        List<ItemCarrinho> carrinhoObjeto;
        List<ComponenteDoPedido> componentes = new ArrayList<>();
        BigDecimal[] valorTotal = {BigDecimal.ZERO};


        if (usuario.getCarrinho() == null || usuario.getCarrinho().equals("{}"))
            carrinhoObjeto = new ArrayList<>();
        else {
            try {
                carrinhoObjeto = objectMapper.readValue(carrinhoJson, new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        ArrayList<Long> idDeProdutosComFretesCalculados = new ArrayList<>();

        carrinhoObjeto.forEach(itemCarrinhoObjeto -> {
            valorTotal[0] = valorTotal[0].add(itemCarrinhoObjeto.getPreco().multiply(BigDecimal.valueOf(itemCarrinhoObjeto.getQuantidade())));
            Optional<Produto> opt = repositorioDeProdutos.findById(itemCarrinhoObjeto.getId());
            if (opt.isEmpty()) {
                throw new ErroPadrao(" Produto não existe mais");
            }

            Estabelecimento estabelecimento = repositorioDeEstabelecimentos.findById(opt.get().getEstabelecimentoId()).get();

            SimulacaoEntregaDto simulacaoEntregaDto = calcularFrete(frete[0], enderecoCliente, estabelecimento);
            componentes.add(new ComponenteDoPedido(itemCarrinhoObjeto.getTitulo(),
                    StringUtils.formatPrice(itemCarrinhoObjeto.getPreco()
                            .multiply(BigDecimal.valueOf(itemCarrinhoObjeto.getQuantidade())))));
            if (idDeProdutosComFretesCalculados.stream().filter(id -> id == itemCarrinhoObjeto.getId()).collect(Collectors.toList()).isEmpty()) {
                componentes.add(new ComponenteDoPedido("Frete", StringUtils.formatPrice(simulacaoEntregaDto.getPrecoEntrega())));
                idDeProdutosComFretesCalculados.add(itemCarrinhoObjeto.getId());
            }
            frete[0].set(frete[0].get().add(simulacaoEntregaDto.getPrecoEntrega()));
            valorTotal[0] = valorTotal[0].add(simulacaoEntregaDto.getPrecoEntrega());
        });
        usuario.setSimulacaoPrecoFinalDaSimulacao(valorTotal[0]);
        usuario.setSimulacaoFrete(frete[0].get());

        repositorioDeUsuario.save(usuario);
        Endereco endereco = enderecoCliente;
        return new TelaEntregaCliente(new EnderecoDto(endereco.getId(), endereco.getEndereco(), endereco.getNumero(), endereco.getComplemento(),
                endereco.getEnderecoCompleto(), endereco.getCep(), endereco.getRotulo(), endereco.getCidade(), endereco.getEstado()),
                componentes, valorTotal[0], StringUtils.formatPrice(valorTotal[0]));
    }

    public SimulacaoEntregaDto calcularFrete(AtomicReference<BigDecimal> frete, Endereco enderecoCliente,
                                             Estabelecimento estabelecimento) {

        DistanciaDto distancia;
        distancia = servicoDoGoogleMaps.calcularDistancia(enderecoCliente, estabelecimento.getEndereco());

        BigDecimal precoFrete = BigDecimal.valueOf(distancia.getDistanciaEmKm())
                .multiply(BigDecimal.valueOf(1.15))
                .multiply(BigDecimal.valueOf(2));
        if (precoFrete.doubleValue() < 15.0) {
            precoFrete = BigDecimal.valueOf(14.90);
        }
        return new SimulacaoEntregaDto(
                precoFrete,
                distancia.getDistanciaEmKm()
        );

    }

    public void postarFormularioPrimeiraCompra(ReqPrimeiraCompra dto, DadosDeCabecalhos dadosDeCabecalhos) {
        Usuario usuario =  repositorioDeUsuario.findById(dadosDeCabecalhos.getIdUsuario()).get();
        usuario.setPrimeiraCompra(false);
        repositorioDeUsuario.save(usuario);

    }



    public TelaDePedidosParaClientes obterPedidos(DadosDeCabecalhos dadosDeCabecalhos) {
        List<Pedido> pedidosDoUsuario = repositorioDePedidos.findByUsuario(dadosDeCabecalhos.getIdUsuario());
        List<PedidoDto> pedido = new ArrayList<>();

        pedidosDoUsuario.forEach( p -> {
            pedido.add(new PedidoDto(
                    p.getId(),
                    p.getImagem(),
                    p.getNome(),
                    StringUtils.formatPrice(p.getValorTotal()),
                    p.getStatus().getTexto(),
                    p.getStatus()
            ));
        });

        return TelaDePedidosParaClientes.builder().pedidos(pedido).build();
    }
}