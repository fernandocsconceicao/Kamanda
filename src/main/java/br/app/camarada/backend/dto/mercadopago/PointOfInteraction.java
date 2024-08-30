package br.app.camarada.backend.dto.mercadopago;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PointOfInteraction {
    public String type;
    public TransactionData transaction_data;
}
