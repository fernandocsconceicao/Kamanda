package br.app.camarada.backend.dto.mercadopago;

import lombok.Builder;

@Builder
public class MercadoPagoPixRequest {
    public Double transaction_amount;
    public String payment_method_id;
    public Payer payer;
    public String description;
    public String date_of_expiration;
    public String notification_url;
    public AdditionalInfo additional_info;
}
