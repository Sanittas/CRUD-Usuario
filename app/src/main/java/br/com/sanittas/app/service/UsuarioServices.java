package br.com.sanittas.app.service;

import br.com.sanittas.app.api.configuration.security.jwt.GerenciadorTokenJwt;
import br.com.sanittas.app.exception.ValidacaoException;
import br.com.sanittas.app.model.Endereco;
import br.com.sanittas.app.model.Usuario;
import br.com.sanittas.app.repository.UsuarioRepository;
import br.com.sanittas.app.service.autenticacao.dto.UsuarioLoginDto;
import br.com.sanittas.app.service.autenticacao.dto.UsuarioTokenDto;
import br.com.sanittas.app.service.endereco.dto.ListaEndereco;
import br.com.sanittas.app.service.usuario.dto.*;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UsuarioServices {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    @Autowired
    private AuthenticationManager authenticationManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioServices.class);

    public List<ListaUsuario> listarUsuarios() {
        try {
            LOGGER.info("Listando usuários");

            var usuarios = repository.findAll();
            List<ListaUsuario> listaUsuarios = new ArrayList<>();

            for (Usuario usuario : usuarios) {
                List<ListaEndereco> listaEnderecos = new ArrayList<>();
                criarDtoEndereco(usuario, listaEnderecos);
                criarDtoUsuarios(usuario, listaEnderecos, listaUsuarios);
            }

            return listaUsuarios;
        } catch (Exception e) {
            LOGGER.error("Erro ao listar usuários: {}", e.getMessage());
            throw e;
        }
    }

    private static void criarDtoUsuarios(Usuario usuario, List<ListaEndereco> listaEnderecos, List<ListaUsuario> listaUsuarios) {
        var usuarioDto = new ListaUsuario(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getSenha(),
                usuario.getTelefone(),
                listaEnderecos

        );
        listaUsuarios.add(usuarioDto);
    }

    private static void criarDtoEndereco(Usuario usuario, List<ListaEndereco> listaEnderecos) {
        for (Endereco endereco : usuario.getEnderecos()) {
            var enderecoDto = new ListaEndereco(
                    endereco.getId(),
                    endereco.getLogradouro(),
                    endereco.getNumero(),
                    endereco.getComplemento(),
                    endereco.getEstado(),
                    endereco.getCidade()

            );
            listaEnderecos.add(enderecoDto);
        }
    }

    public void cadastrar(UsuarioCriacaoDto usuarioCriacaoDto) {
        try {
            LOGGER.info("Cadastrando novo usuário");

            final Usuario novoUsuario = UsuarioMapper.of(usuarioCriacaoDto);

            if (repository.existsByEmail(novoUsuario.getEmail())) {
                throw new ValidacaoException("Email já cadastrado!");
            }
            if (repository.existsByCpf(novoUsuario.getCpf())) {
                throw new ValidacaoException("CPF já cadastrado!");
            }

            String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
            novoUsuario.setSenha(senhaCriptografada);

            repository.save(novoUsuario);
        } catch (Exception e) {
            LOGGER.error("Erro ao cadastrar usuário: {}", e.getMessage());
            throw e;
        }
    }

    public UsuarioTokenDto autenticar(UsuarioLoginDto usuarioLoginDto) {
        try {
            LOGGER.info("Autenticando usuário");

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
        } catch (Exception e) {
            LOGGER.error("Erro ao autenticar usuário: {}", e.getMessage());
            throw e;
        }
    }

    public ListaUsuarioAtualizacao atualizar(Integer id, Usuario dados) {
        try {
            LOGGER.info("Atualizando usuário com ID: {}", id);

            var usuario = repository.findById(id);

            if (usuario.isPresent()) {
                usuario.get().setNome(dados.getNome());
                usuario.get().setEmail(dados.getEmail());
                usuario.get().setCpf(dados.getCpf());
                usuario.get().setSenha(dados.getSenha());

                ListaUsuarioAtualizacao usuarioDto = new ListaUsuarioAtualizacao(
                        usuario.get().getId(),
                        usuario.get().getNome(),
                        usuario.get().getEmail(),
                        usuario.get().getCpf(),
                        usuario.get().getSenha(),
                        usuario.get().getTelefone()
                );

                repository.save(usuario.get());
                return usuarioDto;
            }
            return null;
        } catch (Exception e) {
            LOGGER.error("Erro ao atualizar usuário com ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    public void deletar(Integer id) {
        try {
            LOGGER.info("Deletando usuário com ID: {}", id);

            if (!repository.existsById(id)) {
                throw new ValidacaoException("Usuário não existe!");
            }
            repository.deleteById(id);
        } catch (Exception e) {
            LOGGER.error("Erro ao deletar usuário com ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    public ListaUsuario buscar(Integer id) {
        try {
            LOGGER.info("Buscando usuário com ID: {}", id);

            var usuario = repository.findById(id);

            if (usuario.isEmpty()) {
                throw new ValidacaoException("Usuário não existe!");
            }

            List<ListaEndereco> listaEnderecos = new ArrayList<>();
            criarDtoEndereco(usuario.get(), listaEnderecos);
            ListaUsuario usuarioDto = criarDtoUsuario(usuario, listaEnderecos);
            return usuarioDto;
        } catch (Exception e) {
            LOGGER.error("Erro ao buscar usuário com ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    private static ListaUsuario criarDtoUsuario(Optional<Usuario> usuario, List<ListaEndereco> listaEnderecos) {
        ListaUsuario usuarioDto = new ListaUsuario(
                usuario.get().getId(),
                usuario.get().getNome(),
                usuario.get().getEmail(),
                usuario.get().getCpf(),
                usuario.get().getSenha(),
                usuario.get().getTelefone(),
                listaEnderecos
        );
        return usuarioDto;
    }

    @SneakyThrows
    public String generateToken(String email) {
        try {
            LOGGER.info("Gerando token para o email: {}", email);

            Usuario usuario = repository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

            KeyBasedPersistenceTokenService tokenService = getInstanceFor(usuario);
            Token token = tokenService.allocateToken(usuario.getEmail());

            return token.getKey();
        } catch (Exception e) {
            LOGGER.error("Erro ao gerar token: {}", e.getMessage());
            throw e;
        }
    }

    @SneakyThrows
    public void validarToken(String token) {
        try {
            LOGGER.info("Validando token");

            PasswordTokenPublicData publicData = readPublicData(token);

            if (isExpired(publicData)) {
                throw new RuntimeException("Token expirado");
            }
        } catch (Exception e) {
            LOGGER.error("Erro ao validar token: {}", e.getMessage());
            throw e;
        }
    }

    @SneakyThrows
    public void alterarSenha(NovaSenhaDto novaSenhaDto) {
        try {
            LOGGER.info("Alterando senha com token");

            PasswordTokenPublicData publicData = readPublicData(novaSenhaDto.getToken());

            if (isExpired(publicData)) {
                throw new RuntimeException("Token expirado");
            }

            Usuario usuario = repository.findByEmail(publicData.getEmail())
                    .orElseThrow(RuntimeException::new);

            KeyBasedPersistenceTokenService tokenService = this.getInstanceFor(usuario);
            tokenService.verifyToken(novaSenhaDto.getToken());

            usuario.setSenha(this.passwordEncoder.encode(novaSenhaDto.getNovaSenha()));
            repository.save(usuario);
        } catch (Exception e) {
            LOGGER.error("Erro ao alterar senha: {}", e.getMessage());
            throw e;
        }
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
