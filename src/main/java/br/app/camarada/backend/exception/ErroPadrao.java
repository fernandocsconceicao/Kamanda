package br.app.camarada.backend.exception;

public class ErroPadrao extends RuntimeException{
    public ErroPadrao(){
        super();
    }
    public ErroPadrao(String message){
        super(message);
    }
}
