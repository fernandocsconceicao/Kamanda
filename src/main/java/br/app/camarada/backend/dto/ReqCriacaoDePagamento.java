package br.app.camarada.backend.dto;

import br.app.camarada.backend.enums.MpMetodoDePagamento;
import br.app.camarada.backend.enums.TipoServico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
public class ReqCriacaoDePagamento {
    @Nullable
    private MpMetodoDePagamento metodo;
    @Nullable
    private String cpf;
    @Nullable
    private String celular;
    @NonNull
    private String valor;
    @NonNull
    private TipoServico tipoServico;
}
