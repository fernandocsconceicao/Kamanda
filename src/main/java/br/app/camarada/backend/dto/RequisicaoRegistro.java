package br.app.camarada.backend.dto;

import br.app.camarada.backend.enums.TipoConta;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor

public class RequisicaoRegistro {
    private String email;
    private String senha;
    private String celular;
    private String nome;
    @Nullable
    private TipoConta tipoConta;
//    private CriacaoDeUsuarioClienteDto criacaoDeUsuarioClienteDto;

}
