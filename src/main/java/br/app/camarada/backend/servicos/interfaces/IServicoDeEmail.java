package br.app.camarada.backend.servicos.interfaces;

import org.springframework.mail.SimpleMailMessage;

public interface IServicoDeEmail {
    boolean enviarEmail(SimpleMailMessage mensagem);

}
