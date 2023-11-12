package br.com.sanittas.app.api.configuration.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Componente responsável por gerenciar o ponto de entrada para solicitações de autenticação não autorizadas.
 */
@Component
public class AutenticacaoEntryPoint implements AuthenticationEntryPoint {

    /**
     * Método chamado quando uma exceção de autenticação ocorre.
     *
     * @param request       A solicitação HTTP onde a exceção ocorreu.
     * @param response      A resposta HTTP à qual a exceção será enviada.
     * @param authException A exceção de autenticação que ocorreu.
     * @throws IOException      Se ocorrer um erro de E/S ao manipular a resposta.
     * @throws ServletException Se ocorrer um erro de servlet ao manipular a solicitação.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Verifica se a exceção de autenticação é do tipo BadCredentialsException
        if (authException.getClass().equals(BadCredentialsException.class)) {
            // Se for, envia um erro de não autorizado (401)
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            // Se não for, envia um erro proibido (403)
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
