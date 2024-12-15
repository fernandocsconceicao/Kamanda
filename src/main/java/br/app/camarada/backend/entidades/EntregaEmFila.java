package br.app.camarada.backend.entidades;

import br.app.camarada.backend.enums.StatusEntregaEmFila;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EntregaEmFila {
    @Id
    @GeneratedValue
    private Long id;
    private StatusEntregaEmFila statusEntregaEmFila;
    private Long IdPedido;
    private Long idEntregador;
    private Long idUsuarioCliente;
    private LocalDateTime horaChegada;
    private LocalDateTime horaDoAtendimento;
    private Long idEstabelecimento;
    private Boolean planoA;
    private Long idUsuarioEstabelecimento;
    private String enderecoInicial;
    private String enderecoFinal;
    private String nomeCliente;
    private String nomeEstabelecimento;
    private BigDecimal valorEntrega;
    private BigDecimal valorUbuntu;
    private BigDecimal valorTotal;
}
