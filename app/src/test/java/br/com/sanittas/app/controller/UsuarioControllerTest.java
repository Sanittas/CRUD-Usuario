package br.com.sanittas.app.controller;

import br.com.sanittas.app.model.Usuario;
import br.com.sanittas.app.service.EmailServices;
import br.com.sanittas.app.service.UsuarioServices;
import br.com.sanittas.app.service.autenticacao.dto.UsuarioLoginDto;
import br.com.sanittas.app.service.autenticacao.dto.UsuarioTokenDto;
import br.com.sanittas.app.service.usuario.dto.ListaUsuarioAtualizacaoDto;
import br.com.sanittas.app.service.usuario.dto.NovaSenhaDto;
import br.com.sanittas.app.service.usuario.dto.UsuarioCriacaoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
class UsuarioControllerTest {

    @Mock
    private UsuarioServices usuarioServices;

    @Mock
    private EmailServices emailServices;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {
        UsuarioTokenDto usuarioTokenDto = new UsuarioTokenDto();
        when(usuarioServices.autenticar(any(UsuarioLoginDto.class))).thenReturn(usuarioTokenDto);

        ResponseEntity<UsuarioTokenDto> response = usuarioController.login(new UsuarioLoginDto());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(usuarioTokenDto, response.getBody());
    }

    @Test
    void testListar() {
        List<Usuario> listaUsuarios = Collections.singletonList(new Usuario());
        when(usuarioServices.listarUsuarios()).thenReturn(listaUsuarios);

        ResponseEntity<List<Usuario>> response = usuarioController.listar();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(listaUsuarios, response.getBody());
    }

    @Test
    void testBuscar() {
        Integer userId = 1;
        Usuario usuario = new Usuario();
        when(usuarioServices.buscar(userId)).thenReturn(usuario);

        ResponseEntity<?> response = usuarioController.buscar(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(usuario, response.getBody());
    }

    @Test
    void testCadastrar() {
        UsuarioCriacaoDto usuarioCriacaoDto = new UsuarioCriacaoDto();

        ResponseEntity<Usuario> response = usuarioController.cadastrar(usuarioCriacaoDto);

        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void testAtualizar() {
        Integer userId = 1;
        Usuario usuario = new Usuario();
        when(usuarioServices.atualizar(eq(userId), any(Usuario.class))).thenReturn(usuario);

        ResponseEntity<?> response = usuarioController.atualizar(userId, new Usuario());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(usuario, response.getBody());
    }

    @Test
    void testEsqueciASenha() {
        String email = "test@example.com";
        when(usuarioServices.generateToken(email)).thenReturn("token");

        ResponseEntity<?> response = usuarioController.esqueciASenha(email);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testValidarToken() {
        String token = "token";
        doNothing().when(usuarioServices).validarToken(token);

        ResponseEntity<?> response = usuarioController.validarToken(token);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testAlterarSenha() {
        NovaSenhaDto novaSenhaDto = new NovaSenhaDto();

        ResponseEntity<?> response = usuarioController.alterarSenha(novaSenhaDto);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDeletar() {
        Integer userId = 1;
        doNothing().when(usuarioServices).deletar(userId);

        ResponseEntity<?> response = usuarioController.deletar(userId);

        assertEquals(200, response.getStatusCodeValue());
    }
}
