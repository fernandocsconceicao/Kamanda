package br.app.camarada.backend.entidades;


import br.app.camarada.backend.enums.PlanoEstabelecimento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "estabelecimento")
public class Estabelecimento_Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String cep;
    private String enderecoCompleto;
    private String name;
//    @ManyToMany
//    private List<Totem> totens;
    private Boolean openClosed;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] logo;
    private String telefone;
    private LocalTime inicioExpediente;
    private LocalTime fimExpediente;
    private PlanoEstabelecimento plano;
    @ManyToOne
    private Regiao regiao;
    private Boolean aprovado;
    private Long usuarioId;
    private String description;
    private BigDecimal minOrder;
    @OneToOne(fetch = FetchType.EAGER)
    private Endereco endereco;
    private BigDecimal valorTotalSimulacaoEntrega;
    private BigDecimal valorUbuntuSimulacaoEntrega;
    private BigDecimal valorAReceber;

}
