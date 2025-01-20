package br.app.camarada.backend.servicos;

import br.app.camarada.backend.servicos.interfaces.IServicoDeEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ServicoDeEmail implements IServicoDeEmail {




    @Override
    public boolean enviarEmail(SimpleMailMessage mensagem) {

        mensagem.setFrom("naoresponda@kamanda.app.br");
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.send(mensagem);
        return true;
    }


}
