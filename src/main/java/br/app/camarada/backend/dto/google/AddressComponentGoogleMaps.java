package br.app.camarada.backend.dto.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AddressComponentGoogleMaps {
    @JsonProperty("long_name")
    private String nomeLongo;
    @JsonProperty("short_name")
    private String nomeCurto;
    @JsonProperty("types")
    private List<String> tipos;


}
