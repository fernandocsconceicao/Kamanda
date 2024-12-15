package br.app.camarada.backend.exception;

public class ExcessaoDeEnderecoInvalido extends RuntimeException{
    public ExcessaoDeEnderecoInvalido(){
        super();
    }
    public ExcessaoDeEnderecoInvalido(String message){
        super(message);
    }
}
