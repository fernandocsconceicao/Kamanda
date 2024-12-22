package br.app.camarada.backend.entidades;

import br.app.camarada.backend.dto.PropriedadesDoEstabelecimento;
import br.app.camarada.backend.enums.PlanoEstabelecimento;
import br.app.camarada.backend.utilitarios.ImageUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "estabelecimento")
public class EstabelecimentoReduzido {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String cep;
    private String enderecoCompleto;
    private String name;
    private String cnpj;
    private LocalDate creationDate;
    private Boolean openClosed;
    private BigDecimal minOrder;
    private Float avaliacao;
    private String description;
    private Boolean vegetarianStamp;
    private Boolean veganStamp;
    private String telefone;
    private LocalTime inicioExpediente;
    private LocalTime fimExpediente;
    private PlanoEstabelecimento plano;
    private Double taxa;
    private BigDecimal valorAReceber;
    private Boolean aprovado;
    private Integer limiteDeEntregas;
    private Integer entregasNoPeriodo;
    private Long usuarioId;



}
