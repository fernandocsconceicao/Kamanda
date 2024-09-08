package br.app.camarada.backend.filtros;


import br.app.camarada.backend.entidades.Permissao;
import br.app.camarada.backend.utilitarios.FilterUtil;
import io.jsonwebtoken.MalformedJwtException;

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

//    @Autowired
//    private FilterUtil filterUtil;

    private void filtroDeSeguranca(String caminho, List<Permissao> permissoes, FilterChain filterChain, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServletException {
        if (
                caminho.equals("/perfil/publicar") 
        ) {
//            filterUtil.aplicarSeguranca(permissoes, 1);
            filterChain.doFilter(request, response);
            return;
        }
        if (
                caminho.equals("/usuario/registrar") ||
                        caminho.equals("/produto") ||
                        caminho.equals("/logs") ||
                        caminho.equals("/regiao/definirlocalidade") ||
                        caminho.equals("/estabelecimento/primeiroacesso") ||
                        caminho.equals("/estabelecimento/telas/minhaloja") ||
                        caminho.equals("/estabelecimento/alterarhorariofuncionamento") ||
                        caminho.equals("/estabelecimento/telas/pedidos/list") ||
                        caminho.equals("/estabelecimento/telas/cadastro/completaCadastro") ||
                        caminho.equals("/estabelecimento/telas/solicitacaodeentrega") ||
                        caminho.equals("/estabelecimento/telas/confirmacaodeentrega") ||
                        caminho.equals("/estabelecimento/telas/pedidos/responder") ||
                        caminho.equals("/estabelecimento/telas/listarprodutos") ||
                        caminho.equals("/estabelecimento/telas/entregas") ||
                        caminho.equals("/estabelecimento/telas/editarproduto") ||
                        caminho.equals("/estabelecimento/telas/pedidos/detalhes") ||
                        caminho.equals("/estabelecimento/telas/meuestabelecimento") ||
                        caminho.equals("/estabelecimento/solicitarentrega") ||
                        caminho.equals("/produto/editar") ||
                        caminho.equals("/produto/deletar") ||
                        caminho.equals("/produto/adicionar") ||
                        caminho.equals("/entregas") ||
                        caminho.equals("/produto/excluir") ||
                        caminho.equals("/cliente/endereco/editar")
        ) {
//            filterUtil.aplicarSeguranca(permissoes, 2);
            filterChain.doFilter(request, response);
            return;
        }
        if (
                caminho.equals("/entregador/minhaarea") ||
                        caminho.equals("/entregador/viagens")

        ) {
//            filterUtil.aplicarSeguranca(permissoes, 3);
            filterChain.doFilter(request, response);
            return;
        }
        log.error("Não autorizado");
        response.setStatus(401);
        response.setContentType("application/json");

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (
                (request.getRequestURI().equals("/totem/screens/login")
                        ||
                        request.getRequestURI().equals("/usuario/autenticar"))
                        ||
                        request.getRequestURI().equals("/usuario/registrar")
                        ||
                        request.getRequestURI().equals("/usuario/esqueciasenha/gerarcodigo")
                        ||
                        request.getRequestURI().equals("/usuario/esqueciasenha/confirmarcodigo")
                        ||
                        request.getRequestURI().equals("/usuario/esqueciasenha/enviarnovasenha")
                        ||
                        request.getRequestURI().equals("/regiao/listar")
                        ||
                        request.getRequestURI().equals("/usuario/excluir")

        ) {
            doFilter(request, response, filterChain);
            return;
        }
//
//        try {
//            String token = filterUtil.extrairToken(request);
//            List<Permissao> permissoes = filterUtil.extrairPermissoes(token);
//            filtroDeSeguranca(request.getServletPath(),
//                    permissoes,
//                    filterChain,
//                    request,
//                    response);
//        } catch (NullPointerException |
//                 MalformedJwtException e) {
//            e.printStackTrace();
//            response.setStatus(401);
//        }
    }
}