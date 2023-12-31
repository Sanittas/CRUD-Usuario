package br.com.sanittas.app.service;

import br.com.sanittas.app.api.configuration.security.jwt.GerenciadorTokenJwt;
import br.com.sanittas.app.model.Endereco;
import br.com.sanittas.app.model.Usuario;
import br.com.sanittas.app.repository.UsuarioRepository;
import br.com.sanittas.app.service.autenticacao.dto.UsuarioLoginDto;
import br.com.sanittas.app.service.autenticacao.dto.UsuarioTokenDto;
import br.com.sanittas.app.service.endereco.dto.ListaEndereco;
import br.com.sanittas.app.service.usuario.dto.*;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.SecureRandomFactoryBean;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class UsuarioServices {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    @Autowired
    private AuthenticationManager authenticationManager;

    public List<Usuario> listarUsuarios() {
        log.info("Listando usuários");
        var usuarios = repository.findAll();
        return usuarios;
    }

    public Usuario cadastrar(UsuarioCriacaoDto usuarioCriacaoDto) {
        log.info("Cadastrando novo usuário");

        final Usuario novoUsuario = UsuarioMapper.of(usuarioCriacaoDto);

        if (repository.existsByEmail(novoUsuario.getEmail())) {
            log.error("Email já cadastrado!");
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (repository.existsByCpf(novoUsuario.getCpf())) {
            log.error("CPF já cadastrado!");
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaCriptografada);

        return repository.save(novoUsuario);
    }

    public UsuarioTokenDto autenticar(UsuarioLoginDto usuarioLoginDto) {
        try {
            log.info("Autenticando usuário");

            final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                    usuarioLoginDto.getEmail(), usuarioLoginDto.getSenha());

            final Authentication authentication = this.authenticationManager.authenticate(credentials);

            Usuario usuarioAutenticado =
                    repository.findByEmail(usuarioLoginDto.getEmail())
                            .orElseThrow(() ->
                                    new ResponseStatusException(404, "Email do usuário não cadastrado", null));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            final String jwtToken = gerenciadorTokenJwt.generateToken(authentication);

            return UsuarioMapper.of(usuarioAutenticado, jwtToken);
        } catch (ResponseStatusException e) {
            log.error("Erro ao autenticar usuário: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    public Usuario atualizar(Integer id, Usuario dados) {
        log.info("Atualizando usuário com ID: {}", id);

        var usuario = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        usuario.setNome(dados.getNome());
        usuario.setEmail(dados.getEmail());
        usuario.setCpf(dados.getCpf());
        usuario.setSenha(dados.getSenha());
        usuario.setTelefone(dados.getTelefone());
        return repository.save(usuario);
    }

    public void deletar(Integer id) {
        log.info("Deletando usuário com ID: {}", id);

        if (!repository.existsById(id)) {
            log.error("Usuário com ID {} não encontrado", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }

    public Usuario buscar(Integer id) {
        log.info("Buscando usuário com ID: {}", id);
        var usuario = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return usuario;
    }

    public String generateToken(String email) {
        log.info("Gerando token para o email: {}", email);

        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        try {
            KeyBasedPersistenceTokenService tokenService = getInstanceFor(usuario);
            Token token = tokenService.allocateToken(usuario.getEmail());
            return token.getKey();
        } catch (Exception e) {
            log.error("Erro ao gerar token", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void validarToken(String token) {

        log.info("Validando token");

        PasswordTokenPublicData publicData = readPublicData(token);

        if (isExpired(publicData)) {
            log.error("Token expirado");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    public void alterarSenha(NovaSenhaDto novaSenhaDto) {
        log.info("Alterando senha com token");

        PasswordTokenPublicData publicData = readPublicData(novaSenhaDto.getToken());

        if (isExpired(publicData)) {
            log.error("Token expirado");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        Usuario usuario = repository.findByEmail(publicData.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (usuario.getSenha().equals(novaSenhaDto)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        try {
            KeyBasedPersistenceTokenService tokenService = this.getInstanceFor(usuario);
            tokenService.verifyToken(novaSenhaDto.getToken());
        } catch (Exception e) {
            log.error("Token inválido");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        usuario.setSenha(this.passwordEncoder.encode(novaSenhaDto.getNovaSenha()));
        repository.save(usuario);
    }

    private boolean isExpired(PasswordTokenPublicData publicData) {
        Instant createdAt = new Date(publicData.getCreateAtTimestamp()).toInstant();
        Instant now = new Date().toInstant();
        return createdAt.plus(Duration.ofMinutes(5)).isBefore(now);
    }

    private KeyBasedPersistenceTokenService getInstanceFor(Usuario usuario) throws Exception {
        KeyBasedPersistenceTokenService tokenService = new KeyBasedPersistenceTokenService();
        tokenService.setServerSecret(usuario.getSenha());
        tokenService.setServerInteger(16);
        tokenService.setSecureRandom(new SecureRandomFactoryBean().getObject());
        return tokenService;
    }

    private PasswordTokenPublicData readPublicData(String rawToken) {
        String rawTokenDecoded = new String(Base64.getDecoder().decode(rawToken));
        String[] tokenParts = rawTokenDecoded.split(":");
        Long timestamp = Long.parseLong(tokenParts[0]);
        String email = tokenParts[2];
        return new PasswordTokenPublicData(email, timestamp);
    }
}
