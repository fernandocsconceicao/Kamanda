package br.app.camarada.backend.dto;

import br.app.camarada.backend.dto.mercadopago.MercadoPagoPixResponse;
import br.app.camarada.backend.dto.mercadopago.PointOfInteraction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PixPaymentScreen {
    private PointOfInteraction point_of_interaction;

    public static PixPaymentScreen build(MercadoPagoPixResponse response) {
        return new PixPaymentScreen(response.point_of_interaction);
    }
}
