package br.app.camarada.backend.controladores;

import br.app.camarada.backend.dto.TelaRecargaDeSaldo;
import br.app.camarada.backend.enums.Cabecalhos;
import br.app.camarada.backend.filtros.CustomServletWrapper;
import br.app.camarada.backend.servicos.ServicoDePagamentos;
import br.app.camarada.backend.utilitarios.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@AllArgsConstructor
@RestController
@RequestMapping("telas")
@Slf4j
public class ControladorDeTelas {

    private ServicoDePagamentos servicoDePagamentos;

    @GetMapping("saldo")
    public ResponseEntity<TelaRecargaDeSaldo> obterTelaRegargaDeSaldo(CustomServletWrapper request) {
        Long contaFinanceira = Long.parseLong(request.getHeader(Cabecalhos.CONTA_FINANCEIRA.getValue()));
        System.out.println(contaFinanceira);
        if ( contaFinanceira == null ){

        }
        BigDecimal saldo = servicoDePagamentos.obterSaldo(contaFinanceira);

        return ResponseEntity.ok().body(new TelaRecargaDeSaldo(saldo, StringUtils.formatPrice(saldo)));
    }

}
