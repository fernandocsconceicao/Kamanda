package br.app.camarada.backend.dto.google;

import lombok.Data;

@Data
public class GoogleMapsRowsElement {
    private GoogleMapsRowsElementDistance distance;
    private GoogleMapsRowsElementDistance duration;
    private String status;
}
