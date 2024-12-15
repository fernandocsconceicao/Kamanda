package br.app.camarada.backend.servicos;

import br.app.camarada.backend.dto.*;
import br.app.camarada.backend.entidades.Produto;
import br.app.camarada.backend.entidades.Usuario;
import br.app.camarada.backend.repositorios.RepositorioDeProdutos;
import br.app.camarada.backend.repositorios.RepositorioDeUsuario;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ServicoDaLoja {

    private RepositorioDeUsuario repositorioDeUsuario;
    private RepositorioDeProdutos repositorioDeProdutos;


    @Data
    @AllArgsConstructor
    public class Carrinho {
        private List<ItemCarrinho> itensNoCarrinho;
    }


    public TelaCarrinho obterCarrinho(DadosDeCabecalhos dadosDeCabecalhos) throws JsonProcessingException {
        Usuario usuario = repositorioDeUsuario.findById(dadosDeCabecalhos.getIdUsuario()).get();

        String carrinho = usuario.getCarrinho();
        ObjectMapper objectMapper = new ObjectMapper();
        List<ItemCarrinho> carrinho1 = objectMapper.readValue(carrinho, new TypeReference<>() {
        });

        return new TelaCarrinho(carrinho1);
    }

    public TelaVitrine obterVitrine(DadosDeCabecalhos dadosDeCabecalhos) {

        List<Produto> produtos = repositorioDeProdutos.buscarPorExibicaoEAvaliacao(94L, 10);
        List<ProdutoDto> produtoDtos = new ArrayList<>();

        produtos.forEach(p -> produtoDtos.add(new ProdutoDto(p.getId(), p.getNome(), p.getImagem())));
        produtos.forEach(p -> {p.setDataExibicao(LocalDateTime.now());});

        repositorioDeProdutos.saveAll(produtos);

        return new TelaVitrine(produtoDtos);
    }

    public TelaCarrinho adicionarAoCarrinho(AdicionamentoDeProdutoAoCarrinho dto, DadosDeCabecalhos dadosDeCabecalhos) throws JsonProcessingException {
        Usuario usuario = repositorioDeUsuario.findById(dadosDeCabecalhos.getIdUsuario()).get();
        Optional<Produto> opt = repositorioDeProdutos.findById(dto.getIdProduto());
        if (opt.isPresent()) {
            Produto produto = opt.get();


            String carrinho = usuario.getCarrinho();
            ObjectMapper objectMapper = new ObjectMapper();
            List<ItemCarrinho> carrinho1 = objectMapper.readValue(carrinho, new TypeReference<>() {
            });
            carrinho1.add(new ItemCarrinho(produto.getId(), produto.getNome(), dto.getQuantidade()));
            String s = objectMapper.writeValueAsString(carrinho1);


            usuario.setCarrinho(s);
            repositorioDeUsuario.save(usuario);

            return new TelaCarrinho(carrinho1);
        } else {
            return null;
        }
    }

    public TelaCarrinho retirarProdutoDoCarrinho(AdicionamentoDeProdutoAoCarrinho dto, DadosDeCabecalhos dadosDeCabecalhos) throws JsonProcessingException {
        Usuario usuario = repositorioDeUsuario.findById(dadosDeCabecalhos.getIdUsuario()).get();
        Optional<Produto> opt = repositorioDeProdutos.findById(dto.getIdProduto());
        if (opt.isPresent()) {
            Produto produto = opt.get();


            String carrinho = usuario.getCarrinho();
            ObjectMapper objectMapper = new ObjectMapper();
            List<ItemCarrinho> carrinho1 = objectMapper.readValue(carrinho, new TypeReference<>() {
            });
            List<ItemCarrinho> itemNoCarrinho = carrinho1.stream().filter(p -> p.getId().equals(dto.getIdProduto())).collect(Collectors.toList());
            if (!itemNoCarrinho.isEmpty()) {
                ItemCarrinho itemCarrinho = itemNoCarrinho.get(0);
                itemCarrinho.setQuantidade(itemCarrinho.getQuantidade() - dto.getQuantidade());
                if (itemCarrinho.getQuantidade() >= 0) {
                    int i = carrinho1.indexOf(itemCarrinho);
                    carrinho1.remove(i);
                }

            }
            String s = objectMapper.writeValueAsString(carrinho1);
            usuario.setCarrinho(s);
            repositorioDeUsuario.save(usuario);

            return new TelaCarrinho(carrinho1);
        } else {
            return null;
        }
    }
}
