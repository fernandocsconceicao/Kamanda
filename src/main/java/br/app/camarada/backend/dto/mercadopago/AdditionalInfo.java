package br.app.camarada.backend.dto.mercadopago;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalInfo {
    public ArrayList<Item> items;
}
