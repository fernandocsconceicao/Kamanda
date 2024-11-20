package br.app.camarada.backend.utilitarios;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class StringUtils {
    public static String formatPrice(BigDecimal price) {
        // Define o formato de moeda para o Brasil
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        // Formata o preço para uma string com duas casas decimais
        return nf.format(price);
    }


}
