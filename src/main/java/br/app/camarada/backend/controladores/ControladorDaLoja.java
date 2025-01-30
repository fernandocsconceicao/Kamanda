package br.app.camarada.backend.controladores;

import br.app.camarada.backend.dto.*;
import br.app.camarada.backend.enums.Cabecalhos;
import br.app.camarada.backend.filtros.CustomServletWrapper;
import br.app.camarada.backend.servicos.ServicoDaLoja;
import br.app.camarada.backend.servicos.ServicoParaUsuarios;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loja")

@AllArgsConstructor
@Slf4j
public class ControladorDaLoja {
    private ServicoDaLoja servicoDaLoja;
    private ServicoParaUsuarios servicoParaUsuarios;

    @GetMapping(value = "vitrine")
    public ResponseEntity<TelaVitrine> obterVitrine(CustomServletWrapper request) throws JsonProcessingException {
        long l = Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue()));
        TelaVitrine telaVitrine =
                servicoDaLoja.obterVitrine(
                        DadosDeCabecalhos.builder().idUsuario(l).primeiraCompra(Boolean.parseBoolean(request.getHeader(Cabecalhos.PRIMEIRA_COMPRA.toString()))).build()
                );
        System.out.println(new ObjectMapper().writeValueAsString(telaVitrine.getEstabelecimentos()));
        return ResponseEntity.ok().body(telaVitrine);
    }
    @GetMapping(value = "/pedidos")
    public ResponseEntity<TelaDePedidosParaClientes> obterEntregas(CustomServletWrapper request) {
        long l = Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue()));
        TelaDePedidosParaClientes telaPedidos = servicoDaLoja.obterPedidos(
                DadosDeCabecalhos.builder().idUsuario(l).primeiraCompra(Boolean.parseBoolean(request.getHeader(Cabecalhos.PRIMEIRA_COMPRA.toString()))).build()
        );
        return ResponseEntity.ok().body(telaPedidos);
    }


    @PostMapping("/produto/obter")
    public ResponseEntity<ProductScreen> getProductScreen(@RequestBody ProductScreenRequestBody dto) {
        log.info("Começando request de tela de produtos");
        ProductScreen productScreen = servicoDaLoja.obterTelaDeProduto(dto.getId());
        log.info("Finalizando request de tela de produtos");
        return ResponseEntity.ok(productScreen);
    }

    @GetMapping("carrinho")
    public ResponseEntity<TelaCarrinho> getCartScreen(CustomServletWrapper request) throws JsonProcessingException {
        log.info("Começando request de tela de Carrinho");
        TelaCarrinho screen = servicoDaLoja.obterCarrinho(DadosDeCabecalhos.builder().idUsuario(
                        Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue()))
                ).build()
        );
        System.out.println(screen);
        log.info("Finalizando request de tela de Carrinho");
        return ResponseEntity.ok().body(screen);
    }

    @GetMapping("entrega")
    public ResponseEntity<TelaEntregaCliente> obterTelaDeEntrega(CustomServletWrapper request) {

        try {
            TelaEntregaCliente tela = servicoDaLoja.obterTelaDeEntregaParaCliente(DadosDeCabecalhos.builder()
                    .idUsuario(Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue())))
                    .build());
            return ResponseEntity.ok().body(tela);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(501).build();
        }

    }

    @PostMapping("adicionarprodutoaocarrinho")
    public ResponseEntity<Void> addProductToCart(@RequestBody AdicionamentoDeProdutoAoCarrinho dto, CustomServletWrapper request) throws JsonProcessingException {
        servicoDaLoja.adicionarAoCarrinho(
                dto, DadosDeCabecalhos.builder().idUsuario(
                        Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue()))
                ).build());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/produto/adicionar")
    public ResponseEntity<Void> adicionarProduto(@RequestBody ReqCriacaoDeProduto dto,
                                                 CustomServletWrapper request) {
        servicoDaLoja.adicionarProduto(dto, DadosDeCabecalhos.builder()
                .idEstabecimento(
                        Long.parseLong(
                                request.getHeader(Cabecalhos.ESTABELECIMENTO.getValue())
                        )
                ).build()
        );
        return ResponseEntity.ok().build();
    }


    @PostMapping("/produto/remover")
    public ResponseEntity<Void> removerProduto() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/primeiracompra")
    public ResponseEntity<Void> primeiracompra(@RequestBody ReqPrimeiraCompra dto, CustomServletWrapper request) {

        boolean enderecoEditado = servicoParaUsuarios.editarEndereco(new ReqEdicaoEndereco(
                        dto.getEndereco(),
                        dto.getNumero(),
                        dto.getComplemento(),
                        true,
                        dto.getRotulo(),
                        dto.getCidade(),
                        dto.getEstado(),
                        dto.getCep()),
                DadosDeCabecalhos.builder().idEndereco(Long.parseLong(request.getHeader(Cabecalhos.ENDERECO.getValue())))
                        .build()
        );
        if(enderecoEditado){
            servicoDaLoja.postarFormularioPrimeiraCompra(dto, DadosDeCabecalhos.builder()
                    .idUsuario(
                            Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue()))
                    ).build());
        }else {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

}
