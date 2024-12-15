package br.app.camarada.backend.controladores;

import br.app.camarada.backend.dto.ReqAlterarHorarioFuncionamento;
import br.app.camarada.backend.dto.ReqPrimeiroAcessoEstabelecimento;
import br.app.camarada.backend.dto.ResPrimeiroAcessoEstabelecimento;
import br.app.camarada.backend.dto.SolicitacaoDeEntrega;
import br.app.camarada.backend.enums.Cabecalhos;
import br.app.camarada.backend.enums.TipoServico;
import br.app.camarada.backend.exception.ExcessaoDeEnderecoInvalido;
import br.app.camarada.backend.exception.PlanoIncompativelException;
import br.app.camarada.backend.filtros.CustomServletWrapper;
import br.app.camarada.backend.servicos.ServicoDeEstabelecimentos;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estabelecimento")
@Slf4j
@AllArgsConstructor
public class ControladorDeEstabelecimentos {
    private ServicoDeEstabelecimentos servicoDeEstabelecimentos;

    @PostMapping("/solicitarentrega")
    public ResponseEntity<Void> solicitarEntrega(@RequestBody SolicitacaoDeEntrega dto, CustomServletWrapper request)  {
        try{
            dto.setTipoServico(TipoServico.ENTREGA);
            servicoDeEstabelecimentos.solicitarEntrega(
                    dto,
                    Long.parseLong(request.getHeader(Cabecalhos.ESTABELECIMENTO.getValue())),
                    Long.parseLong(request.getHeader(Cabecalhos.USUARIO.getValue())),
                    null,
                    null);
        }catch (PlanoIncompativelException e){
            return  ResponseEntity.status(406).build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/primeiroacesso")
    public ResponseEntity<ResPrimeiroAcessoEstabelecimento> primeiroAcesso(@RequestBody ReqPrimeiroAcessoEstabelecimento dto, CustomServletWrapper request) {
        log.info("Iniciando requisição  de postagem de primeiro acesso");
        ResPrimeiroAcessoEstabelecimento resPrimeiroAcessoEstabelecimento = null;
        try{
            dto.setIdDeEstabelecimento(Long.parseLong(request.getHeader(Cabecalhos.ESTABELECIMENTO.getValue())));
             resPrimeiroAcessoEstabelecimento = servicoDeEstabelecimentos.primeiroAcesso(dto);
        }catch (ExcessaoDeEnderecoInvalido e){
            ResponseEntity.status(HttpStatus.CREATED).build();
        }
        log.info("Finalizando requisição  de postagem de primeiro acesso");
        return ResponseEntity.status(HttpStatus.OK).body(resPrimeiroAcessoEstabelecimento);
    }

    @GetMapping("/alternardisponibilidade")
    public ResponseEntity<Boolean> alternardisponibilidade(CustomServletWrapper request) {
        log.info("Iniciando requisição  de postagem de alteração de disponibilidade");
        Boolean disponibilidade = servicoDeEstabelecimentos.alternardisponibilidade(Long.parseLong(request.getHeader(Cabecalhos.ESTABELECIMENTO.getValue())));
        log.info("Finalizando requisição  de postagem de alteração de disponibilidade");
        return ResponseEntity.status(200).body(disponibilidade);
    }

    @PutMapping("/alterarhorariofuncionamento")
    public ResponseEntity<Boolean> alterarhorariofuncionamento(@RequestBody ReqAlterarHorarioFuncionamento dto, CustomServletWrapper request) {
        log.info("Iniciando requisição  de postagem de troca de horario de funcionamento");
        Boolean disponibilidade = servicoDeEstabelecimentos.alterarHorarioFuncionamento(dto, Long.parseLong(request.getHeader(Cabecalhos.ESTABELECIMENTO.getValue())));
        log.info("Finalizando requisição  de postagem de troca de horario de funcionamento");
        return ResponseEntity.status(200).body(disponibilidade);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> tokenExpirado() {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Token Expirado");
    }
}
