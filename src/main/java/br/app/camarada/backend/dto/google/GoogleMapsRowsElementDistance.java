package br.app.camarada.backend.dto.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GoogleMapsRowsElementDistance {

    @JsonProperty("text")
    private String distancia;
    @JsonProperty("value")
    private Integer metros;
}
