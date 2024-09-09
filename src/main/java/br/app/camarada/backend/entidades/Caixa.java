package br.app.camarada.backend.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@Data
public class Caixa {
    @Id
    @GeneratedValue
    private Long id;
    private BigDecimal totalArrecadado;
    private BigDecimal aRepasar;
    private BigDecimal balanco;

    public Caixa() {
        this.id= 1L;
        this.totalArrecadado = BigDecimal.ZERO;
        this.aRepasar=BigDecimal.ZERO;
        this.balanco = BigDecimal.ZERO;
    }
}
