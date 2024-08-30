package br.app.camarada.backend.utilitarios;


import br.app.camarada.backend.entidades.Permissao;
import br.app.camarada.backend.entidades.Usuario;
import br.app.camarada.backend.enums.PermissaoEnum;
import br.app.camarada.backend.servicos.JwtService;
import br.app.camarada.backend.servicos.ServicoParaUsuarios;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class FilterUtil {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ServicoParaUsuarios userService;


    public List<Permissao> extrairPermissoes(String token) {
        String nomeDeUsuario = jwtService.extrairNomeDeUsuario(token);
        Usuario user = userService.obterUsuario(nomeDeUsuario);
        return user.getPermissoes();
    }

    public String extrairToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        } else {
            throw   new SecurityException("Token Inválido");
        }
    }

    public boolean aplicarSeguranca(List<Permissao> permissoes, int nivelDeSeguranca) {
        switch (nivelDeSeguranca) {
            case 0:
                return true;

            case 1: {
                for (Permissao permissao : permissoes) {
                    if (
                            permissao.getAuthority().equals(PermissaoEnum.USUARIO.name())
                    ) {
                        return true;
                    }
                }
            }

            default:
                return false;


        }
    }
}
