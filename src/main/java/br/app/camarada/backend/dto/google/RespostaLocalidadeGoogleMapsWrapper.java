package br.app.camarada.backend.dto.google;

import lombok.Data;

import java.util.List;

@Data
public class RespostaLocalidadeGoogleMapsWrapper {
    private List<RespostaLocalidadeGoogleMaps> results;
    private String status;
}
