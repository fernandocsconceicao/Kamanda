package br.app.camarada.backend.filtros;



import br.app.camarada.backend.entidades.Usuario;
import br.app.camarada.backend.enums.Cabecalhos;
import br.app.camarada.backend.servicos.JwtService;
import br.app.camarada.backend.servicos.ServicoParaUsuarios;
import br.app.camarada.backend.utilitarios.FilterUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Filtro para avaliar token Jwt
 * Extensão...O filtro extende OncePerRequestFilter para fazer com que o filtro seja executado uma vez em todas as requests
 * <p>
 * Jwt tokens são compostos de um header{algorith,type}, uma payload data{n informations,extra claims} e uma signature(that assure security of the token by identifying the sender)
 */

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Lazy
    @Autowired
    private JwtService jwtService;
    @Lazy
    @Autowired
    private ServicoParaUsuarios servicoParaUsuarios;

    @Autowired
    @Lazy
    private FilterUtil filterUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            if (
                    (
                            request.getRequestURI().equals("/usuario/autenticar"))
                            ||
                            request.getRequestURI().equals("/usuario/registrar")
                            ||
                            request.getRequestURI().equals("/usuario/esqueciminhasenha")
                            ||
                            request.getRequestURI().equals("/usuario/esqueciasenha/gerarcodigo")
                            ||
                            request.getRequestURI().equals("/usuario/esqueciasenha/confirmarcodigo")
                            ||
                            request.getRequestURI().equals("/usuario/esqueciasenha/enviarnovasenha")
                            ||
                            request.getRequestURI().equals("/usuario/excluir")
                            ||
                            request.getRequestURI().equals("/regiao/listar")


            ) {

                log.info("Filtro de Autenticação- Endpoint livre");
                Usuario userDetails = this.servicoParaUsuarios.loadUserByUsername("admin@admin.com");

                CustomServletWrapper requisicao = adicionarHeaders(request, userDetails);

                if (jwtService.isTokenValid(jwtService.gerarToken(userDetails), userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
                doFilter(requisicao, response, filterChain);
            } else {
                log.info("Filtro de Autenticação- Endpoint autenticado " + request.getRequestURI());
                String token;
                String userEmail;
                try {

                    token = filterUtil.extrairToken(request);
                    userEmail = jwtService.extrairNomeDeUsuario(token);
                } catch (NullPointerException | MalformedJwtException e) {
                    log.info("Erro " + e.getMessage());
                    response.setStatus(403);
                    return;
                }
                Usuario userDetails = this.servicoParaUsuarios.loadUserByUsername(userEmail);
                CustomServletWrapper requisicao = adicionarHeaders(request, userDetails);
                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
                System.out.println("filtro");
                filterChain.doFilter(requisicao, response);
            }
        } catch (ExpiredJwtException e) {
            response.setStatus(402);

        }
    }

    private CustomServletWrapper adicionarHeaders(HttpServletRequest request, Usuario userDetails) {
        CustomServletWrapper httpReq = new CustomServletWrapper(request);

        httpReq.addHeader(Cabecalhos.EMAIL.getValue(), userDetails.getEmail());
        httpReq.addHeader(Cabecalhos.TIPO_CONTA.getValue(), userDetails.getTipoConta().toString());
        httpReq.addHeader(Cabecalhos.NOME.getValue(), userDetails.getNome());
        if (userDetails.getContaFinanceira() != null)
            httpReq.addHeader(Cabecalhos.CONTA_FINANCEIRA.getValue(), userDetails.getContaFinanceira().getId().toString());



        httpReq.addHeader(Cabecalhos.USUARIO.getValue(), userDetails.getId().toString());



        return httpReq;
    }
}
