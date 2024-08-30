package br.app.camarada.backend.dto.mercadopago;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MPPaymentMethod {
    public String id;
    public String type;
    public String issuer_id;
}
