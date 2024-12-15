package br.app.camarada.backend.entidades;

import br.app.camarada.backend.dto.CriacaoDoEntregador;
import br.app.camarada.backend.enums.StatusEntregador;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entregador {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long usuarioId;
    private String nome;
    @NotNull
    private String celular;
    private BigDecimal totalArrecadado;
    private Integer viagens;
    private Float avaliacao;
    private byte[] image;
    private LocalDate dataDoRegistro;
    private StatusEntregador status;
    private Long entregaEmQueSeEstaTrabalhando;
    private Long regiaoPrimariaId;
    private LocalDate dataExclusao;


    public static Entregador build(CriacaoDoEntregador propriedades) {
        return new Entregador(
                null,
                null,
                propriedades.getNome(),
                propriedades.getCelular(),
                null,
                0,
                null,
                null,
                LocalDate.now(),
                StatusEntregador.OFFLINE,
                null,
                null,
                null
        );
    }
}