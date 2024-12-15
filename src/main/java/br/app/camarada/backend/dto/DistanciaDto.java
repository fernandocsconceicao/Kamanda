package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DistanciaDto {
    private Double distanciaEmKm;
    private String distanciaGoogle;
}
