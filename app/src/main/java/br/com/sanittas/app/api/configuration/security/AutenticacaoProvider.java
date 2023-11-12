package br.com.sanittas.app.api.configuration.security;

import br.com.sanittas.app.service.autenticacao.dto.AutenticacaoService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.logging.Logger;

/**
 * Provedor de autenticação personalizado que verifica as credenciais do usuário.
 */
public class AutenticacaoProvider implements AuthenticationProvider {

    private static final Logger LOGGER = Logger.getLogger(AutenticacaoProvider.class.getName());
    private final AutenticacaoService autenticacaoService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Construtor do provedor de autenticação.
     *
     * @param autenticacaoService Serviço de autenticação.
     * @param passwordEncoder    Codificador de senha.
     */
    public AutenticacaoProvider(AutenticacaoService autenticacaoService, PasswordEncoder passwordEncoder) {
        this.autenticacaoService = autenticacaoService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Método para autenticar um usuário com base nas credenciais fornecidas.
     *
     * @param authentication O objeto de autenticação.
     * @return Um token de autenticação se a autenticação for bem-sucedida.
     * @throws AuthenticationException Se a autenticação falhar.
     */
    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();

        LOGGER.info("Autenticando usuário: " + username);
        UserDetails userDetails = this.autenticacaoService.loadUserByUsername(username);

        if (userDetails != null && this.passwordEncoder.matches(password, userDetails.getPassword())) {
            // Se as credenciais forem válidas, retorna um token de autenticação
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } else {
            // Se as credenciais forem inválidas, lança uma exceção de credenciais inválidas
            throw new BadCredentialsException("Usuário ou senha inválidos");
        }
    }

    /**
     * Verifica se o provedor pode autenticar o tipo de classe fornecido.
     *
     * @param authentication A classe de autenticação a ser verificada.
     * @return Verdadeiro se o provedor puder autenticar a classe, falso caso contrário.
     */
    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
