package br.app.camarada.backend.entidades;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Estatisticas {
    @Id
    @GeneratedValue
    private Long id;

    private Long publicações;
}
