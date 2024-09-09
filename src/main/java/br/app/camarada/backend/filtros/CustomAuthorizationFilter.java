package br.app.camarada.backend.filtros;


import br.app.camarada.backend.entidades.Permissao;
import br.app.camarada.backend.utilitarios.FilterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Value("auth-secret")
    private String secret;

    @Autowired
    private FilterUtil filterUtil;

    private void filtroDeSeguranca(String caminho, List<Permissao> permissoes, FilterChain filterChain, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServletException {
        if (
                caminho.equals("/perfil/publicar") || caminho.equals("/usuario/registrar")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        log.error("Não autorizado");
        response.setStatus(401);
        response.setContentType("application/json");

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        doFilter(request, response, filterChain);
        return;
//        if (
//                (request.getRequestURI().equals("/totem/screens/login")
//                        ||
//                        request.getRequestURI().equals("/usuario/autenticar"))
//                        ||
//                        request.getRequestURI().equals("/usuario/registrar")
//                        ||
//                        request.getRequestURI().equals("/usuario/esqueciasenha/gerarcodigo")
//                        ||
//                        request.getRequestURI().equals("/usuario/esqueciasenha/confirmarcodigo")
//                        ||
//                        request.getRequestURI().equals("/usuario/esqueciasenha/enviarnovasenha")
//
//                        ||
//                        request.getRequestURI().equals("/usuario/excluir")
//
//        ) {
////            filterUtil.aplicarSeguranca(permissoes, 1);
//           return;
//        }else {
//            log.info("Não autenticado");
//        }
    }
}