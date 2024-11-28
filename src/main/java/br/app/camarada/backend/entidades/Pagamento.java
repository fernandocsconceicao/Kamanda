package br.app.camarada.backend.entidades;


import br.app.camarada.backend.enums.FormaDePagamento;
import br.app.camarada.backend.enums.StatusPagamento;
import br.app.camarada.backend.enums.TipoServico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Pagamento {
    @Id
    @GeneratedValue()
    private Long id;
    @Enumerated
    private StatusPagamento status;
    @Column(nullable = false)
    private Double valor;
    private LocalDateTime horaDoPedido;
    private Long pixId;
    private Long usuarioId;
    private BigDecimal valorUbuntu;
    private TipoServico tipoServico;
    @Enumerated
    private FormaDePagamento formaDePagamento;


}
