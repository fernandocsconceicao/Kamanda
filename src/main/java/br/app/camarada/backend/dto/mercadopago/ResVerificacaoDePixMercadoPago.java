package br.app.camarada.backend.dto.mercadopago;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResVerificacaoDePixMercadoPago {

    public Long id;
    public String status;

    public static ResVerificacaoDePixMercadoPago build(MercadoPagoPixResponse verificarPagamento) {
        return new ResVerificacaoDePixMercadoPago(verificarPagamento.id, verificarPagamento.getStatus());
    }
}
