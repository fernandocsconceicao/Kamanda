package br.app.camarada.backend.exception;

public class NomeDeUsuarioExistente extends Exception {
    public NomeDeUsuarioExistente(){
        super();
    }
    public NomeDeUsuarioExistente(String message){
        super(message);
    }
}
