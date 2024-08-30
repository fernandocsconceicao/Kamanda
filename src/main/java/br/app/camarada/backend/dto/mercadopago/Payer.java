package br.app.camarada.backend.dto.mercadopago;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Payer {
    public String first_name;
    public String last_name;
    public String email;
}
