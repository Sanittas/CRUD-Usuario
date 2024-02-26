package br.com.sanittas.app.service;

import br.com.sanittas.app.model.EnderecoUsuario;
import br.com.sanittas.app.model.Usuario;
import br.com.sanittas.app.repository.EnderecoRepository;
import br.com.sanittas.app.repository.UsuarioRepository;
import br.com.sanittas.app.service.endereco.dto.EnderecoCriacaoDto;
import br.com.sanittas.app.service.endereco.dto.ListaEndereco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class EnderecoUsuarioServicesTest {

    @Mock
    private EnderecoRepository repository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private EnderecoServices enderecoServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarEnderecosPorUsuario_WithValidUserId_ReturnsListOfAddresses() {
        // Arrange
        Integer userId = 1;
        Usuario usuario = new Usuario();
        usuario.setId(userId);
        EnderecoUsuario enderecoUsuario1 = new EnderecoUsuario();
        enderecoUsuario1.setId(1L);
        enderecoUsuario1.setLogradouro("Rua A");
        // ... Initialize other fields
        EnderecoUsuario enderecoUsuario2 = new EnderecoUsuario();
        enderecoUsuario2.setId(2L);
        enderecoUsuario2.setLogradouro("Rua B");
        // ... Initialize other fields

        usuario.setEnderecoUsuarios(List.of(enderecoUsuario1, enderecoUsuario2));
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuario));

        // Act
        List<ListaEndereco> result = enderecoServices.listarEnderecosPorUsuario(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Rua A", result.get(0).getLogradouro());
        assertEquals("Rua B", result.get(1).getLogradouro());
    }

    @Test
    void cadastrarEnderecoUsuario_WithValidData_CallsRepositorySave() {
        // Arrange
        Integer userId = 1;
        EnderecoCriacaoDto enderecoDto = new EnderecoCriacaoDto();
        // Preencha os dados do DTO de criação de endereço

        Usuario usuario = new Usuario();
        usuario.setId(userId);

        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuario));

        // Act
        enderecoServices.cadastrarEnderecoUsuario(enderecoDto, userId);

        // Assert
        verify(repository, times(1)).save(Mockito.any());
    }

    @Test
    void cadastrarEnderecoUsuario_WithInvalidUser_ThrowsResponseStatusException() {
        // Arrange
        Integer userId = 1;
        EnderecoCriacaoDto enderecoDto = new EnderecoCriacaoDto();
        // Preencha os dados do DTO de criação de endereço

        when(usuarioRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> enderecoServices.cadastrarEnderecoUsuario(enderecoDto, userId));
    }

    @Test
    void atualizar_WithValidId_ReturnsUpdatedAddress() {
        // Arrange
        Long enderecoId = 1L;
        EnderecoCriacaoDto enderecoDto = new EnderecoCriacaoDto();
        // Preencha os dados do DTO de atualização de endereço

        EnderecoUsuario enderecoUsuario = new EnderecoUsuario();
        enderecoUsuario.setId(enderecoId);
        // Defina outros campos do endereço

        when(repository.findById(enderecoId)).thenReturn(Optional.of(enderecoUsuario));

        // Act
        ListaEndereco result = enderecoServices.atualizar(enderecoDto, enderecoId);

        // Assert
        assertNotNull(result);
        // Verifique se os campos foram atualizados corretamente
        assertEquals(enderecoDto.getLogradouro(), result.getLogradouro());
        // Verifique se outros campos foram atualizados corretamente
    }

    @Test
    void atualizar_WithInvalidId_ThrowsResponseStatusException() {
        // Arrange
        Long enderecoId = 1L;
        EnderecoCriacaoDto enderecoDto = new EnderecoCriacaoDto();
        // Preencha os dados do DTO de atualização de endereço

        when(repository.findById(enderecoId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> enderecoServices.atualizar(enderecoDto, enderecoId));
    }

    @Test
    void deletarEndereco_WithValidId_DeletesAddress() {
        // Arrange
        Long enderecoId = 1L;
        when(repository.existsById(enderecoId)).thenReturn(true);

        // Act
        enderecoServices.deletarEndereco(enderecoId);

        // Assert
        verify(repository, times(1)).deleteById(enderecoId);
    }

    @Test
    void deletarEndereco_WithInvalidId_ThrowsResponseStatusException() {
        // Arrange
        Long enderecoId = 1L;
        when(repository.existsById(enderecoId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> enderecoServices.deletarEndereco(enderecoId));
    }
}
