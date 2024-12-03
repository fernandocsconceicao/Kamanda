package br.app.camarada.backend.controladores;

import br.app.camarada.backend.dto.PixPaymentScreen;
import br.app.camarada.backend.dto.ReqCriacaoDePagamento;
import br.app.camarada.backend.dto.ReqVerificacaoPix;
import br.app.camarada.backend.dto.RequisicaoPagarComSaldo;
import br.app.camarada.backend.dto.mercadopago.MercadoPagoPixResponse;
import br.app.camarada.backend.enums.Cabecalhos;
import br.app.camarada.backend.enums.TipoConta;
import br.app.camarada.backend.exception.ErroPadrao;
import br.app.camarada.backend.exception.ExcessaoDePagamentoCancelado;
import br.app.camarada.backend.filtros.CustomServletWrapper;
import br.app.camarada.backend.servicos.ServicoDePagamentos;
import feign.FeignException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@AllArgsConstructor
@RequestMapping("pagamento")
@Slf4j
public class ControladorDePagamento {
    private ServicoDePagamentos servicoDePagamentos;

    @RequestMapping("verificar")
    public ResponseEntity<MercadoPagoPixResponse> verificarPagamentoPix(CustomServletWrapper request, @RequestBody ReqVerificacaoPix dto) {
        try {
            MercadoPagoPixResponse body = servicoDePagamentos.verificarPagamento(
                    Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue())),

                    dto.getCodigo()
            );
            return ResponseEntity.ok().body(body);
        } catch (ExcessaoDePagamentoCancelado ep) {
            return ResponseEntity.status(HttpStatus.GONE).build();
        }
    }

    @RequestMapping("cancelar")
    public ResponseEntity<MercadoPagoPixResponse> cancelar(CustomServletWrapper request) {
        try {
            servicoDePagamentos.cancelarPagamentoPendente(
                    Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue()))
            );
            return ResponseEntity.ok().build();
        } catch (ExcessaoDePagamentoCancelado ep) {
            return ResponseEntity.status(HttpStatus.GONE).build();
        }
    }

    @PostMapping("/saldo")
    public ResponseEntity<Void> pagarComSaldo(CustomServletWrapper request, @RequestBody RequisicaoPagarComSaldo dto) {

        Long idEstabelecimento= null;
        String tipoConta = (request.getHeader(Cabecalhos.TIPO_CONTA.getValue()));
        long idContaFinanceira = Long.parseLong(request.getHeader(Cabecalhos.CONTA_FINANCEIRA.getValue()));
        long idUsuario = Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue()));

        try {
            servicoDePagamentos.pagarComSaldo(
                    idContaFinanceira,
                    dto,
                    idEstabelecimento,
                    idUsuario);

        } catch (ClassNotFoundException e) {
            return ResponseEntity.status(209).build();
        } catch (InterruptedException e) {

        }
        return ResponseEntity.ok().build();
    }
    @PostMapping("/pix/criar")
    public ResponseEntity<PixPaymentScreen> getPaymentAuthScreen(@RequestBody ReqCriacaoDePagamento dto,
                                                                 CustomServletWrapper request)  {
        try {
            Long idUsuario = Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue()));


            MercadoPagoPixResponse mercadoPagoPixResponse = servicoDePagamentos.criarPagamentoPix(
                    dto,
                    request.getHeader(Cabecalhos.EMAIL.getValue()),
                    request.getHeader(Cabecalhos.NOME.getValue()),
                    idUsuario,
                    null,
                    null,
                    null,
                    null

            );
            return ResponseEntity.ok().body(PixPaymentScreen.build(
                            mercadoPagoPixResponse
                    )
            );
        } catch (IllegalArgumentException | OptimisticLockingFailureException e) {
            String message = new StringBuilder
                    ("Erro ao criar pedido: ").append(e.getMessage())
                    .toString();
            log.error(message);
            throw new RuntimeException(message);
        } catch (NoSuchElementException e) {
            String message = new StringBuilder
                    ("Totem inválido: ").append(e.getMessage())
                    .toString();
            log.error(message);
            throw new ErroPadrao(message);
        } catch (FeignException error) {
            log.error(error.getMessage());
            throw new ErroPadrao();

        }

    }
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> tokenExpirado() {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Token Expirado");

    }

}
