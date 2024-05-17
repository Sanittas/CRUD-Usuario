package br.com.sanittas.app.service;

import br.com.sanittas.app.model.Usuario;
import br.com.sanittas.app.repository.UsuarioRepository;
import br.com.sanittas.app.service.usuario.dto.NovaSenhaDto;
import br.com.sanittas.app.service.usuario.dto.PasswordTokenPublicData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UsuarioServices {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public List<Usuario> listarUsuarios() {
        log.info("Listando usuários");
        var usuarios = repository.findAll();
        return usuarios;
    }

    public Usuario atualizar(Integer id, Usuario dados) {
        log.info("Atualizando usuário com ID: {}", id);

        var usuario = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        usuario.setNome(dados.getNome());
        usuario.setEmail(dados.getEmail());
        usuario.setCpf(dados.getCpf());
        usuario.setSenha(passwordEncoder.encode(dados.getSenha()));
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

//    public String generateToken(String email) {
//        log.info("Gerando token para o email: {}", email);
//
//        Usuario usuario = repository.findByEmail(email)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        try {
//            KeyBasedPersistenceTokenService tokenService = getInstanceFor(usuario);
//            Token token = tokenService.allocateToken(usuario.getEmail());
//            return token.getKey();
//        } catch (Exception e) {
//            log.error("Erro ao gerar token", e);
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
//        }
//    }

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
//        try {
//            KeyBasedPersistenceTokenService tokenService = this.getInstanceFor(usuario);
//            tokenService.verifyToken(novaSenhaDto.getToken());
//        } catch (Exception e) {
//            log.error("Token inválido");
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }
        usuario.setSenha(novaSenhaDto.getNovaSenha());
        repository.save(usuario);
    }

    private boolean isExpired(PasswordTokenPublicData publicData) {
        Instant createdAt = new Date(publicData.getCreateAtTimestamp()).toInstant();
        Instant now = new Date().toInstant();
        return createdAt.plus(Duration.ofMinutes(5)).isBefore(now);
    }

//    private KeyBasedPersistenceTokenService getInstanceFor(Usuario usuario) throws Exception {
//        KeyBasedPersistenceTokenService tokenService = new KeyBasedPersistenceTokenService();
//        tokenService.setServerSecret(usuario.getSenha());
//        tokenService.setServerInteger(16);
//        tokenService.setSecureRandom(new SecureRandomFactoryBean().getObject());
//        return tokenService;
//    }

    private PasswordTokenPublicData readPublicData(String rawToken) {
        String rawTokenDecoded = new String(Base64.getDecoder().decode(rawToken));
        String[] tokenParts = rawTokenDecoded.split(":");
        Long timestamp = Long.parseLong(tokenParts[0]);
        String email = tokenParts[2];
        return new PasswordTokenPublicData(email, timestamp);
    }

    public Usuario buscarPorEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
