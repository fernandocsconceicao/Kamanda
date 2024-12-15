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
public class Estabelecimento {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String cep;
    private String enderecoCompleto;
    private String name;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Produto> produtos;
    private String cnpj;
    private LocalDate creationDate;
//    @ManyToMany
//    private List<Totem> totens;
    private Boolean openClosed;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] logo;
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
    @ManyToOne
    private Regiao regiao;
    private Boolean aprovado;

    private Integer limiteDeEntregas;
    private Integer entregasNoPeriodo;
    private Long usuarioId;
    @OneToOne(fetch = FetchType.LAZY)
    private Endereco endereco;

    public static Estabelecimento build(PropriedadesDoEstabelecimento dto, Endereco endereco)  {

        try {
            return new Estabelecimento(
                    null,
                    null,
                    null,
                    dto.getNome(),
                    null,
                    dto.getCnpj(),
                    LocalDate.now(),
                    false,
                    ImageUtils.createTestImage(),
                    null,
                    0.0f,
                    null,
                    false,
                    false,
                    dto.getTelefone(),
                    null,
                    null,
                    dto.getPlanoEstabelecimento(),
                    dto.getPlanoEstabelecimento().getTax(),
                    BigDecimal.ZERO,
                    null,
                    false,
                    null,
                    null,
                    null,
                    endereco

                );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
