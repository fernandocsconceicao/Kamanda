package br.app.camarada.backend.dto.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RespostaLocalidadeGoogleMaps {

    private List<AddressComponentGoogleMaps> address_components;
    @JsonProperty("geometry")
    private GeometriaGoogleMaps geometriaGoogleMaps;

    @JsonProperty("place_id")
    private String placeId;

    @JsonProperty("formatted_address")
    private String enderecoCompleto;

    @JsonProperty("plus_code")
    private PlusCodeGoogleMaps plusCodeGoogleMaps;

    @JsonProperty("types")
    private List<String> tipos;
}
