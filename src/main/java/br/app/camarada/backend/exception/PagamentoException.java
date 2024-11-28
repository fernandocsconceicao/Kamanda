package br.app.camarada.backend.exception;

public class PagamentoException extends RuntimeException{
    PagamentoException(){
        super();
    }
    public PagamentoException(String message){
        super(message);
    }
}
