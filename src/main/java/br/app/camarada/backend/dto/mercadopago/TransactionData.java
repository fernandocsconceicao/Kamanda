package br.app.camarada.backend.dto.mercadopago;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionData {
    public String qr_code;
    public String transaction_id;
    public String ticket_url;
    public String qr_code_base64;
}
