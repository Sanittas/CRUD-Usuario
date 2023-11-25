package br.com.sanittas.app.controller;

import br.com.sanittas.app.service.EnderecoServices;
import br.com.sanittas.app.service.endereco.dto.EnderecoCriacaoDto;
import br.com.sanittas.app.service.endereco.dto.ListaEndereco;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para operações relacionadas a endereços.
 */
@RestController
@SecurityRequirement(name = "bearer-key") // Requisito de segurança para autenticação JWT
@RequestMapping("/enderecos")
@Setter
public class EnderecoController {

    @Autowired
    private EnderecoServices usuarioServices; // Serviço de endereços por usuário
    @Autowired
    private EnderecoServices enderecoServices; // Serviço de endereços

    /**
     * Obtém uma lista de endereços associados a um usuário.
     *
     * @param id_usuario O ID do usuário.
     * @return Uma ResponseEntity contendo a lista de endereços ou uma resposta vazia.
     */
    @GetMapping("/usuarios/{id_usuario}")
    public ResponseEntity<List<ListaEndereco>> listarEnderecosPorUsuario(@PathVariable Integer id_usuario) {
        try {
            var response = usuarioServices.listarEnderecosPorUsuario(id_usuario);
            if (!response.isEmpty()) {
                return ResponseEntity.status(200).body(response);
            }
            return ResponseEntity.status(204).build(); // Sem conteúdo
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(400).build(); // Requisição inválida
        }
    }

    /**
     * Cadastra um novo endereço para um usuário.
     *
     * @param endereco     O DTO de criação de endereço.
     * @param usuario_id   O ID do usuário.
     * @return Uma ResponseEntity indicando o sucesso ou falha da operação.
     */
    @PostMapping("/usuarios/{usuario_id}")
    public ResponseEntity<Void> cadastrarEnderecoUsuario(@RequestBody EnderecoCriacaoDto endereco, @PathVariable Integer usuario_id) {
        try {
            enderecoServices.cadastrarEnderecoUsuario(endereco, usuario_id);
            return ResponseEntity.status(201).build(); // Criado com sucesso
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(400).build(); // Requisição inválida
        }
    }

    /**
     * Atualiza um endereço existente.
     *
     * @param enderecoCriacaoDto O DTO de criação de endereço com as informações atualizadas.
     * @param id                 O ID do endereço a ser atualizado.
     * @return Uma ResponseEntity contendo o endereço atualizado ou uma resposta de falha.
     */
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<ListaEndereco> atualizarEndereco(@RequestBody EnderecoCriacaoDto enderecoCriacaoDto, @PathVariable Long id) {
        try {
            var endereco = enderecoServices.atualizar(enderecoCriacaoDto, id);
            return ResponseEntity.status(200).body(endereco);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(400).build(); // Requisição inválida
        }
    }

    /**
     * Deleta um endereço associado a um usuário.
     *
     * @param id O ID do endereço a ser deletado.
     * @return Uma ResponseEntity indicando o sucesso ou falha da operação.
     */
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> deletarEnderecoUsuario(@PathVariable Long id) {
        try {
            enderecoServices.deletarEndereco(id);
            return ResponseEntity.status(200).build(); // Ok
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(400).build(); // Requisição inválida
        }
    }
}
