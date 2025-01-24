package br.app.camarada.backend.servicos;

import br.app.camarada.backend.servicos.interfaces.IServicoDeEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ServicoDeEmail implements IServicoDeEmail {


    private JavaMailSenderImpl javaMailSender;


    @Override
    public boolean enviarEmail(SimpleMailMessage mensagem) {

        System.out.println(javaMailSender.getHost() + "senha" + javaMailSender.getPassword());
        System.out.println(javaMailSender.getUsername());
        javaMailSender.setUsername("naoresponda");
        mensagem.setFrom("naoresponda@kamanda.app.br");
        javaMailSender.send(mensagem);
        return true;
    }


}
