package br.app.camarada.backend.dto.mercadopago;

public class TransactionDetails {
    public Object payment_method_reference_id;
    public Object acquirer_reference;
    public Integer net_received_amount;
    public Double total_paid_amount;
    public Integer overpaid_amount;
    public Integer installment_amount;
//    public Object financial_institution;
    public Object payable_deferral_period;
    public Object bank_transfer_id;
    public Object transaction_id;
}
