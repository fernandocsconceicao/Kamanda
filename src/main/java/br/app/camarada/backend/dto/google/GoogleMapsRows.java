package br.app.camarada.backend.dto.google;

import lombok.Data;

import java.util.List;
@Data
public class GoogleMapsRows {
    private List<GoogleMapsRowsElement> elements;
}
