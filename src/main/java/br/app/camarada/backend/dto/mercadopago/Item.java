package br.app.camarada.backend.dto.mercadopago;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item {
    public String id;
    public String title;
    public String description;
    public Integer quantity;
    public Double unit_price;
}
