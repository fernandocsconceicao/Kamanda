package br.app.camarada.backend.dto.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ViewPortGoogleMaps {
    @JsonProperty("northest")
    private Coordenadas nordeste;
    @JsonProperty("soutwest")
    private Coordenadas sudoeste;

}
