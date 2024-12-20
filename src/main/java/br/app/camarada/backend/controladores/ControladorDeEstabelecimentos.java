package br.app.camarada.backend.controladores;

import br.app.camarada.backend.dto.*;
import br.app.camarada.backend.enums.Cabecalhos;
import br.app.camarada.backend.enums.CategoriaProduto;
import br.app.camarada.backend.enums.Cores;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    @GetMapping("/minhaloja")
    public ResponseEntity<MeuPerfilScreen> getProductScreen(CustomServletWrapper request) {
        MeuPerfilScreen meuEstabelecimentoScreen = servicoDeEstabelecimentos.obterTelaMinhaLoja(Long.parseLong(request.getHeader(Cabecalhos.ESTABELECIMENTO.getValue())));
        return ResponseEntity.ok().body(meuEstabelecimentoScreen);
    }

    @PutMapping("/minhaloja")
    public ResponseEntity<Void> postProductScreen(@RequestBody PostagemDeEdicaoDeEstabelecimento dto) {
        try {
            servicoDeEstabelecimentos.editarEstabelecimento(dto);
        } catch (ExcessaoDeEnderecoInvalido e) {
            return ResponseEntity.status(201).build();
        }
        return ResponseEntity.ok().build();
    }
    @GetMapping("/meuestabelecimento")
    public ResponseEntity<TelaMeuEstabelecimento> meuEstabelecimento(CustomServletWrapper request) {
        TelaMeuEstabelecimento meuEstabelecimentoScreen = servicoDeEstabelecimentos.obterTelaEstabelecimento(Long.parseLong(request.getHeader(Cabecalhos.ESTABELECIMENTO.getValue())));
        return ResponseEntity.ok().body(meuEstabelecimentoScreen);
    }

    @PostMapping("listarprodutos")
    public ResponseEntity<ResTelaDeListagemDeProdutos> teladeEdicaoDeProduto(CustomServletWrapper request) {
        log.info("Iniciando requisição  de listagem de Produtos de estabelecimento ");
        List<ProdutoParaEstabelecimento> resposta = servicoDeEstabelecimentos
                .listarProdutosDeEstabelecimentos((Long.parseLong(request.getHeader(Cabecalhos.ESTABELECIMENTO.getValue()))));

        List<CategoriaDto> categorias = Arrays.stream(CategoriaProduto.values())
                .map(categoriaProduto -> new CategoriaDto(categoriaProduto.getNome(), categoriaProduto.name()))
                .collect(Collectors.toList());
        List<CoresDto> cores = Arrays.stream(Cores.values())
                .map(cor -> new CoresDto(cor.getCor(), cor.getCodigoHex()))
                .collect(Collectors.toList());

        log.info("Finalizando requisição  de listagem de Produtos de estabelecimento");
        return ResponseEntity.ok().body(new ResTelaDeListagemDeProdutos(resposta, categorias,cores));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> tokenExpirado() {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Token Expirado");
    }
}
