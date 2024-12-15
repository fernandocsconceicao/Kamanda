package br.app.camarada.backend.dto.google;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PlusCodeGoogleMaps {
    @JsonProperty("compound_code")
    private String compoundCode;

    @JsonProperty("global_code")
    private String globalCode;
}
