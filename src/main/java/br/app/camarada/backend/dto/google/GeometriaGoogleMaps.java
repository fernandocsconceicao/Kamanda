package br.app.camarada.backend.dto.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GeometriaGoogleMaps {
    @JsonProperty("location")
    private Coordenadas coordenadas;

//    @JsonProperty("location_type")
//    private TipoDeLocalidadeGoogleMaps tipoDeLocalidade;

    @JsonProperty("viewport")
    private ViewPortGoogleMaps viewPortGoogleMaps;
}
