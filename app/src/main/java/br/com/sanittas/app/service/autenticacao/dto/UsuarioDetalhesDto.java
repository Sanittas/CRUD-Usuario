package br.com.sanittas.app.service.autenticacao.dto;

import br.com.sanittas.app.model.Usuario;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Implementação de UserDetails que representa os detalhes do usuário para fins de autenticação.
 */
@Getter
@Setter
public class UsuarioDetalhesDto implements UserDetails {
    private final String nome;
    private final String email;
    private final String senha;

    /**
     * Construtor que recebe uma instância de Usuario e extrai os detalhes necessários.
     *
     * @param usuario Objeto Usuario contendo informações sobre o usuário
     */
    public UsuarioDetalhesDto(Usuario usuario) {
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.senha = usuario.getSenha();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Este método deve ser implementado se a aplicação exigir controle de autorizações.
        return null;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
