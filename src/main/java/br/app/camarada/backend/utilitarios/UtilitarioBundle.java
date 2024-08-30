package br.app.camarada.backend.utilitarios;

import br.app.camarada.backend.enums.TextosBundle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
@Slf4j
public class UtilitarioBundle {
    public static String obterMensagem(TextosBundle propriedade) {
        ResourceBundle messages = ResourceBundle.getBundle("messages");

        try {
            return messages.getString(propriedade.toString());
        } catch (NullPointerException exception) {
            exception.printStackTrace();
            log.debug("Key da Resource Bundle inválida");
            throw exception;
        }

    }
}
