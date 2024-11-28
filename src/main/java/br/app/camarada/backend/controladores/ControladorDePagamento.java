package br.app.camarada.backend.controladores;

import br.app.camarada.backend.dto.ReqVerificacaoPix;
import br.app.camarada.backend.dto.RequisicaoPagarComSaldo;
import br.app.camarada.backend.dto.mercadopago.MercadoPagoPixResponse;
import br.app.camarada.backend.enums.Cabecalhos;
import br.app.camarada.backend.exception.ExcessaoDePagamentoCancelado;
import br.app.camarada.backend.filtros.CustomServletWrapper;
import br.app.camarada.backend.servicos.ServicoDePagamentos;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("pagamento")
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

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> tokenExpirado() {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Token Expirado");

    }

}
