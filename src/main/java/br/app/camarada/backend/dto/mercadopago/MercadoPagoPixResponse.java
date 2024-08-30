package br.app.camarada.backend.dto.mercadopago;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MercadoPagoPixResponse {

    public Long id;
    public Date date_created;
    public Date date_of_expiration;
    public String operation_type;
    public String payment_method_id;
    public String payment_type_id;
    public MPPaymentMethod payment_method;
    public String status;

    public String description;
    public boolean live_mode;
    public Integer taxes_amount;
    public PointOfInteraction point_of_interaction;
    public Payer payer;
    public AdditionalInfo additional_info;
    public Double transaction_amount;
    public Integer installments;
    public TransactionDetails transaction_details;
    public String notification_url;
}
