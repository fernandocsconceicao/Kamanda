package br.app.camarada.backend.servicos;

import br.app.camarada.backend.client.GoogleMapsClient;
import br.app.camarada.backend.dto.*;
import br.app.camarada.backend.entidades.Endereco;
import br.app.camarada.backend.entidades.Estabelecimento;
import br.app.camarada.backend.entidades.Produto;
import br.app.camarada.backend.entidades.Usuario;
import br.app.camarada.backend.exception.ErroPadrao;
import br.app.camarada.backend.repositorios.RepositorioDeEnderecos;
import br.app.camarada.backend.repositorios.RepositorioDeEstabelecimentos;
import br.app.camarada.backend.repositorios.RepositorioDeProdutos;
import br.app.camarada.backend.repositorios.RepositorioDeUsuario;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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


    public ProductScreen obterTelaDeProduto(Long id) {
        Produto produto = repositorioDeProdutos.findById(id).get();
        return ProductScreen.build(produto, getProductProperties(produto), produto.getPreco());
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
//    public void atualizarPropriedadesDoEstabelecimento(Produto produto) {
//
//        List<Produto> establishmentProdutos = productRepository.findByEstablishmentId(produto.getEstabelecimento().getId());
//        boolean veganStamp = false;
//        boolean vegetarianStamp = false;
//
//        Optional<Produto> optionalProduct = establishmentProdutos.stream().filter(Produto::getVeganStamp).findFirst();
//        Optional<Produto> vegetarianProducts = establishmentProdutos.stream().filter(Produto::getVegetarianStamp).findFirst();
//
//        if (optionalProduct.isPresent()) {
//            veganStamp = true;
//        }
//
//        if (vegetarianProducts.isPresent()) {
//            vegetarianStamp = true;
//        }
//        //TODO: Economizar chamadas ao banco
//        Estabelecimento estabelecimento = establishmentRepository.findById(produto.getEstabelecimento().getId()).get();
//        estabelecimento.setVeganStamp(veganStamp);
//        estabelecimento.setVegetarianStamp(vegetarianStamp);
//        establishmentRepository.save(estabelecimento);
//    }


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


        return new TelaCarrinho(carrinhoComImagens, StringUtils.formatPrice(valorTotal[0]));
    }

    public TelaVitrine obterVitrine(DadosDeCabecalhos dadosDeCabecalhos) {

        List<Produto> produtos = repositorioDeProdutos.buscarPorExibicaoEAvaliacao(10);
        List<ProdutoDto> produtoDtos = new ArrayList<>();

        produtos.forEach(p -> produtoDtos.add(new ProdutoDto(p.getId(), p.getNome(), p.getImagem(), StringUtils.formatPrice(p.getPreco()))));
        produtos.forEach(p -> {
            p.setDataExibicao(LocalDateTime.now());
        });

        repositorioDeProdutos.saveAll(produtos);

        return new TelaVitrine(produtoDtos);
    }

    public void adicionarAoCarrinho(AdicionamentoDeProdutoAoCarrinho dto, DadosDeCabecalhos dadosDeCabecalhos) throws JsonProcessingException {
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
                        }
                        itemAdicionado.set(true);
                    }
                });
                if (!itemAdicionado.get())
                    carrinhoObjeto.add(new ItemCarrinho(produto.getId(), produto.getNome(), itemCarrinho.getQuantidade(), produto.getPreco()));

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

    public TelaEntregaCliente obterTelaDeEntregaParaCliente(DadosDeCabecalhos dadosDeCabecalhos) {

        Usuario usuario = repositorioDeUsuario.findById(dadosDeCabecalhos.getIdUsuario()).get();
        Endereco enderecoCliente = usuario.getEndereco();

        AtomicReference<BigDecimal> frete = new AtomicReference<>(new BigDecimal(0));

        //lista de estabelecimentos do carrinho
        String carrinhoJson = usuario.getCarrinho();

        ObjectMapper objectMapper = new ObjectMapper();
        List<ItemCarrinho> carrinhoObjeto;
        List<ComponenteDoPedido> componentes = new ArrayList<>();
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
        carrinhoObjeto.forEach(itemCarrinhoObjeto -> {
            valorTotal[0] = valorTotal[0].add(itemCarrinhoObjeto.getPreco().multiply(BigDecimal.valueOf(itemCarrinhoObjeto.getQuantidade())));
            Optional<Produto> opt = repositorioDeProdutos.findById(itemCarrinhoObjeto.getId());
            if (opt.isEmpty()) {
                throw new ErroPadrao(" Produto não existe mais");
            }
            Estabelecimento estabelecimento = opt.get().getEstabelecimento();

            SimulacaoEntregaDto simulacaoEntregaDto = calcularFrete(frete, enderecoCliente, estabelecimento);
            componentes.add(new ComponenteDoPedido(itemCarrinhoObjeto.getTitulo(),
                    StringUtils.formatPrice(itemCarrinhoObjeto.getPreco()
                            .multiply(BigDecimal.valueOf(itemCarrinhoObjeto.getQuantidade())))));
            componentes.add(new ComponenteDoPedido("Frete", StringUtils.formatPrice(simulacaoEntregaDto.getPrecoEntrega())));
            valorTotal[0] = valorTotal[0].add(simulacaoEntregaDto.getPrecoEntrega());
        });
        Endereco endereco = enderecoCliente;
        return new TelaEntregaCliente(new EnderecoDto(endereco.getId(), endereco.getEndereco(), endereco.getNumero(), endereco.getComplemento(),
                endereco.getEnderecoCompleto(), endereco.getCep(), endereco.getRotulo(), endereco.getCidade(), endereco.getEstado()),
                componentes, valorTotal[0], StringUtils.formatPrice(valorTotal[0]));
    }

    public SimulacaoEntregaDto calcularFrete(AtomicReference<BigDecimal> frete, Endereco enderecoCliente,
                                             Estabelecimento estabelecimento) {

        DistanciaDto distancia;
        distancia = servicoDoGoogleMaps.calcularDistancia(enderecoCliente, estabelecimento.getEndereco());
        return new SimulacaoEntregaDto(

                BigDecimal.valueOf(distancia.getDistanciaEmKm())
                        .multiply(BigDecimal.valueOf(1.15))
                        .multiply(BigDecimal.valueOf(2))

                ,
                distancia.getDistanciaEmKm()
        );
    }
}