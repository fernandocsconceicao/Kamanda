package br.app.camarada.backend.entidades;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Preferencias {
    @Id
    @GeneratedValue
    private Long id;
    private Long usuarioId;

}

