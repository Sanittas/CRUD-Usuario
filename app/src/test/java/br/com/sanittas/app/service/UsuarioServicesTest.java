package br.com.sanittas.app.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.sanittas.app.api.configuration.security.jwt.GerenciadorTokenJwt;
import br.com.sanittas.app.model.Usuario;
import br.com.sanittas.app.repository.UsuarioRepository;
import br.com.sanittas.app.service.autenticacao.dto.UsuarioLoginDto;
import br.com.sanittas.app.service.autenticacao.dto.UsuarioTokenDto;
import br.com.sanittas.app.service.usuario.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class UsuarioServicesTest {

    private UsuarioServices usuarioServices;
    private UsuarioRepository usuarioRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private GerenciadorTokenJwt gerenciadorTokenJwt;
    private AuthenticationManager authenticationManager;
    private Usuario usuarioMock;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        gerenciadorTokenJwt = mock(GerenciadorTokenJwt.class);
        authenticationManager = mock(AuthenticationManager.class);
        usuarioServices = new UsuarioServices(usuarioRepository, passwordEncoder, gerenciadorTokenJwt, authenticationManager);
        usuarioMock = new Usuario(1, "Nome", "12345678901", "teste@gmail.com","39956707848", "senha", new ArrayList<>());
    }

    @Test
    void listarUsuarios_WithValidData_ReturnsListOfUsers() {
        // Arrange
        Usuario usuario1 = new Usuario(); // Crie um objeto Usuario com dados fictícios
        Usuario usuario2 = new Usuario(); // Crie outro objeto Usuario com dados fictícios
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(usuario1);
        usuarios.add(usuario2);

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Act
        List<Usuario> listaUsuarios = usuarioServices.listarUsuarios();

        // Assert
        assertNotNull(listaUsuarios);
        assertEquals(2, listaUsuarios.size()); // Verifique se a lista retornada tem tamanho 2
        // Verifique outras condições necessárias
    }

    @Test
    void cadastrar_DeveRetornarConflitoQuandoEmailJaExistir() {
        when(usuarioRepository.existsByEmail(usuarioMock.getEmail())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                usuarioServices.cadastrar(new UsuarioCriacaoDto("", "teste@gmail.com", "", "", "")));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

    @Test
    void cadastrar_DeveRetornarConflitoQuandoCpfJaExistir() {
        when(usuarioRepository.existsByEmail(usuarioMock.getEmail())).thenReturn(false);
        when(usuarioRepository.existsByCpf(usuarioMock.getCpf())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                usuarioServices.cadastrar(new UsuarioCriacaoDto("", "", "39956707848", "", "")));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

    @Test
    void cadastrar_DeveCadastrarUsuarioComSucesso() {
        when(usuarioRepository.existsByEmail(usuarioMock.getEmail())).thenReturn(false);
        when(usuarioRepository.existsByCpf(usuarioMock.getCpf())).thenReturn(false);
        when(passwordEncoder.encode(usuarioMock.getSenha())).thenReturn("senhaEncriptada");

        assertDoesNotThrow(() -> usuarioServices.cadastrar(new UsuarioCriacaoDto()));
        verify(usuarioRepository, times(1)).save(any());
    }

    // Testes para o método autenticar

    @Test
    void autenticar_DeveRetornarUsuarioAutenticadoComSucesso() {
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(usuarioRepository.findByEmail(usuarioMock.getEmail())).thenReturn(Optional.of(usuarioMock));
        when(gerenciadorTokenJwt.generateToken(any())).thenReturn("token");

        UsuarioTokenDto usuarioTokenDto = usuarioServices.autenticar(new UsuarioLoginDto(usuarioMock.getEmail(), usuarioMock.getSenha()));

        assertNotNull(usuarioTokenDto);
        assertEquals(usuarioMock.getId(), usuarioTokenDto.getUserId());
        assertEquals("token", usuarioTokenDto.getToken());
    }

    @Test
    void autenticar_DeveLancarExcecaoQuandoEmailNaoEstaCadastrado() {
        when(usuarioRepository.findByEmail(usuarioMock.getEmail())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                usuarioServices.autenticar(new UsuarioLoginDto(usuarioMock.getEmail(), usuarioMock.getSenha())));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

}
