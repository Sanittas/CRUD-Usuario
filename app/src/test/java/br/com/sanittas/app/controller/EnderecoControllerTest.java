package br.com.sanittas.app.controller;

import br.com.sanittas.app.service.EnderecoServices;
import br.com.sanittas.app.service.endereco.dto.EnderecoCriacaoDto;
import br.com.sanittas.app.service.endereco.dto.ListaEndereco;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class EnderecoControllerTest {

    @Mock
    private EnderecoServices enderecoServices;

    @InjectMocks
    private EnderecoController enderecoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        enderecoController.setUsuarioServices(enderecoServices);
    }

    @Test
    void testListarEnderecosPorUsuario() {
        // Mock dos serviços para listar endereços por usuário
        when(enderecoServices.listarEnderecosPorUsuario(anyInt())).thenReturn(Collections.singletonList(new ListaEndereco()));


        ResponseEntity<List<ListaEndereco>> response = enderecoController.listarEnderecosPorUsuario(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testCadastrarEnderecoUsuario() {
        EnderecoCriacaoDto enderecoCriacaoDto = new EnderecoCriacaoDto();

        ResponseEntity<Void> response = enderecoController.cadastrarEnderecoUsuario(enderecoCriacaoDto, 1);

        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void testAtualizarEndereco() {
        EnderecoCriacaoDto enderecoCriacaoDto = new EnderecoCriacaoDto();

        when(enderecoServices.atualizar(any(), anyLong())).thenReturn(new ListaEndereco());

        ResponseEntity<ListaEndereco> response = enderecoController.atualizarEndereco(enderecoCriacaoDto, 1L);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDeletarEnderecoUsuario() {
        ResponseEntity<Void> response = enderecoController.deletarEnderecoUsuario(1L);

        assertEquals(200, response.getStatusCodeValue());
    }
}
