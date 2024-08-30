package br.app.camarada.backend.entidades;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContaFinanceira {
    @GeneratedValue
    @Id
    private Long id;
    @Column(nullable = false, name = "id_usuario")
    private Long idUsuario;

    private BigDecimal saldo;
    private String historico;
    private LocalDateTime dataCriacao;
}
