package br.app.camarada.backend.dto;

import br.app.camarada.backend.enums.TipoConta;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class UserCreationDto {
    private String email;
    private String senha;
    private String celular;
    private String nome;
    private TipoConta tipoConta;
//    private CriacaoDeUsuarioClienteDto criacaoDeUsuarioClienteDto;

}
