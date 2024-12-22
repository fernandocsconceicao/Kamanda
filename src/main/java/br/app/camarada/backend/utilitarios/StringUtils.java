package br.app.camarada.backend.utilitarios;

import br.app.camarada.backend.dto.ItemCarrinho;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StringUtils {
    public static String formatPrice(BigDecimal price) {
        // Define o formato de moeda para o Brasil
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        // Formata o preço para uma string com duas casas decimais
        return nf.format(price);
    }


    public static List<ItemCarrinho> transformarCarrinhoEmObj(String carrinho) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<ItemCarrinho> carrinhoObj;
        if (carrinho == null || carrinho.equals("{}"))
            carrinhoObj = new ArrayList<>();
        else {
            carrinhoObj = objectMapper.readValue(carrinho, new TypeReference<>() {
            });
        }
        return carrinhoObj;
    }

    public static String tranformarObjetoEmJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(obj);

        return json;
}
}
