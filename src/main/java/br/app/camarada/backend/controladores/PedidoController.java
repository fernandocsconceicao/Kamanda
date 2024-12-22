package br.app.camarada.backend.controladores;

import br.app.camarada.backend.filtros.CustomServletWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/pedido")
@RestController
@Slf4j
public class PedidoController {

//    @Autowired
//    private ServicoDePedidos servicoDePedidos;
//
//    @PostMapping("avaliarPedido")
//    public ResponseEntity<ResTelaAvaliacaoPedido> obterTelaDeAvaliacaoDePedido(@RequestBody ReqAvaliacaoDePedido dto) throws JsonProcessingException {
//        log.info("Começando request efetivacao de avaliacao de Pedido");
//        List<AvaliacaoDeProdutoDto> telaDeAvaliacaoDePedido = screenService.buscarTelaDeAvaliacaoDePedido(dto.getIdPedido());
//        log.info("Finalizando request efetivacao de avaliacao de Pedido");
//
//        return ResponseEntity.ok().body(ResTelaAvaliacaoPedido.build(telaDeAvaliacaoDePedido));
//    }
//    @GetMapping("/listar")
//    public ResponseEntity<ResTelaDeListagemDePedidos> listarPedidos(CustomServletWrapper request) {
//        List<Pedido> pedidos = servicoDePedidos.encontrarPorUsuario(
//                Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue()))
//        );
//        List<Pedido> pedidosOrdenados = pedidos.stream()
//                .sorted((p1, p2) -> p2.getOrderTime().compareTo(p1.getOrderTime()))
//                .collect(Collectors.toList());
//        return ResponseEntity.ok().body(ResTelaDeListagemDePedidos.build(pedidosOrdenados));
//    }
//
//    @PostMapping("avaliarPedido")
//    public ResponseEntity<Void> obterTelaDeAvaliacaoDePedido(@RequestBody AvaliacaoDePedido dto) {
//        log.info("Começando request de tela de produtos");
//        servicoDePedidos.avaliarPedidos(dto);
//        log.info("Finalizando request de tela de produtos");
//        return ResponseEntity.ok().build();
//    }
//
//    @ExceptionHandler(ExpiredJwtException.class)
//    public ResponseEntity<String> tokenExpirado() {
//        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Token Expirado");
//
//    }
}
