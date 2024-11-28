package br.app.camarada.backend.exception;

public class ExcessaoDePagamentoCancelado extends RuntimeException{
    public ExcessaoDePagamentoCancelado(){
        super();
    }
    public ExcessaoDePagamentoCancelado(String message){
        super(message);
    }
}
