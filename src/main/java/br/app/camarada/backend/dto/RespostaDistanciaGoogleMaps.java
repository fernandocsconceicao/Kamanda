package br.app.camarada.backend.dto;

import br.app.camarada.backend.dto.google.GoogleMapsRows;
import lombok.Data;

import java.util.List;

@Data
public class RespostaDistanciaGoogleMaps {
    private List<String> destination_addresses;
    private List<String> origin_addresses;
    private List<GoogleMapsRows> rows;
    private String status;
}
