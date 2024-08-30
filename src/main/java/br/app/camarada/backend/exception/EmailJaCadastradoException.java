package br.app.camarada.backend.exception;

public class EmailJaCadastradoException extends RuntimeException{
    public EmailJaCadastradoException(){
        super();
    }
    public EmailJaCadastradoException(String message){
        super(message);
    }
}
