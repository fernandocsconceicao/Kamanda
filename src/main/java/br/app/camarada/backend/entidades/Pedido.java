package br.app.camarada.backend.entidades;

import br.app.camarada.backend.enums.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class    Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated
    private StatusPedido status;
    private Long usuario;
    private String cpf;
    private String nomeCliente;
    private LocalDateTime orderTime;
    private LocalDateTime acceptedTime;
    private LocalDateTime deliveryTime;
    private LocalDateTime prepareTime;
    private Integer expectedTime;
    private Long idDoEstabelecimento;
    private Long cartId;
    private String nome;
    private String celular;
    private BigDecimal valorTotal; // TODO: Trocar para valor total dos produtos do pedido
    private Long regiaoId;
    private String titulo;
    private Long entregadorId;
    private String enderecoEstabelecimento;
    private String enderecoCliente;
    private String rejeitadoPelosEntregadores;
    private Long oferecidoParaEntregador;
    private BigDecimal valorCorrida;
    @ManyToOne
    private Pagamento pagamento;

    @Lob
    private String produtos;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] imagem;
    private BigDecimal valorEstabelecimento;
    private Boolean compensacaoEstabelecimento;
    private BigDecimal valorUbuntu;

}
