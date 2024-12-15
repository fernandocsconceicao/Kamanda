package br.app.camarada.backend.exception;

public class PlanoIncompativelException extends RuntimeException{
    PlanoIncompativelException(){
        super();
    }
    public PlanoIncompativelException(String message){
        super(message);
    }
}
