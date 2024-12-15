package br.app.camarada.backend.dto;

public class EstablishmentCreationException extends RuntimeException{
    EstablishmentCreationException(){
        super();
    }
    public EstablishmentCreationException(String message){
        super(message);
    }
}
