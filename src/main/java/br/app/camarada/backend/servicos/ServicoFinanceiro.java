package br.app.camarada.backend.servicos;

import br.app.camarada.backend.entidades.Caixa;
import br.app.camarada.backend.entidades.Pedido;
import br.app.camarada.backend.repositorios.CaixaRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ServicoFinanceiro {
    private final CaixaRepositorio caixaRepositorio;


    public BigDecimal incorporarValorDePedidoAoCaixa(BigDecimal valor, Pedido pedido, Double taxaDeServico) {
        Caixa caixa = caixaRepositorio.findById(1L).get();
        caixa.setBalanco(caixa.getBalanco().add(valor));
//        caixa.setARepassarParaEntregador(caixa.getARepassarParaEntregador().add(pedido.getValorCorrida()));
//        caixa.setARepassarParaEstabelecimento(caixa.getARepassarParaEntregador().add(pedido.getValorTotal().multiply(BigDecimal.valueOf(0.92))));


        caixaRepositorio.save(caixa);
        return caixa.getBalanco();
    }
}
