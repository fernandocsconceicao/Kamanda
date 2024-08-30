package br.app.camarada.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespostaHoraAtualWorldTime {
    private ZonedDateTime datetime;
    private Integer day_of_week;
}
