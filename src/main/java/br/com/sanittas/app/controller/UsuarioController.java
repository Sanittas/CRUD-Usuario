package br.com.sanittas.app.controller;

import br.com.sanittas.app.model.Usuario;
import br.com.sanittas.app.service.UsuarioServices;
import br.com.sanittas.app.service.usuario.dto.NovaSenhaDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@Slf4j
public class UsuarioController {

    @Autowired
    private UsuarioServices services; // Serviços relacionados a usuários

    /**
     * Lista todos os usuários.
     *
     * @return Uma ResponseEntity contendo a lista de usuários ou uma resposta vazia.
     */
    @GetMapping("/")
    public ResponseEntity<List<Usuario>> listar() {
        try {
            var response = services.listarUsuarios();
            if (!response.isEmpty()) {
                return ResponseEntity.status(200).body(response);
            }
            return ResponseEntity.status(204).build(); // Sem conteúdo
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * Busca um usuário pelo ID.
     *
     * @param id O ID do usuário a ser buscado.
     * @return Uma ResponseEntity contendo o usuário encontrado ou uma resposta de falha.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(@PathVariable Integer id) {
        try {
            Usuario usuario = services.buscar(id);
            return ResponseEntity.status(200).body(usuario);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatusCode());
        }
    }

    /**
     * Atualiza um usuário existente.
     *
     * @param id     O ID do usuário a ser atualizado.
     * @param dados  O objeto contendo as informações atualizadas do usuário.
     * @return Uma ResponseEntity contendo o usuário atualizado ou uma resposta de falha.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Integer id, @RequestBody @Valid Usuario dados) {
        try {
            var usuario = services.atualizar(id, dados);
            return ResponseEntity.status(200).body(usuario);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatusCode());
        }
    }

//    @PostMapping("/esqueci-senha")
//    public ResponseEntity<Void> esqueciASenha(@RequestParam String email) {
//        try {
////            String token = services.generateToken(email);
////            emailServices.enviarEmailComToken(email, token);
//            return ResponseEntity.status(200).build();
//        } catch (ResponseStatusException e) {
//            log.info(e.getLocalizedMessage());
//            throw new ResponseStatusException(e.getStatusCode());
//        }
//    }

    /**
     * Valida um token para redefinição de senha.
     *
     * @param token O token a ser validado.
     * @return Uma ResponseEntity indicando o sucesso ou falha da operação.
     */
    @GetMapping("/validarToken/{token}")
    public ResponseEntity<Void> validarToken(@PathVariable String token) {
        try {
            services.validarToken(token);
            return ResponseEntity.status(200).build();
        } catch (ResponseStatusException e) {
            throw new  ResponseStatusException(e.getStatusCode());
        }
    }

    /**
     * Altera a senha de um usuário.
     *
     * @param novaSenhaDto O DTO contendo a nova senha e o token de validação.
     * @return Uma ResponseEntity indicando o sucesso ou falha da operação.
     */
    @PutMapping("/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@RequestBody @Valid NovaSenhaDto novaSenhaDto) {
        try {
            services.alterarSenha(novaSenhaDto);
            return ResponseEntity.status(200).build();
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatusCode());
        }
    }

    /**
     * Deleta um usuário.
     *
     * @param id O ID do usuário a ser deletado.
     * @return Uma ResponseEntity indicando o sucesso ou falha da operação.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        try {
            services.deletar(id);
            return ResponseEntity.status(200).build();
        } catch (ResponseStatusException e) {
            log.error("Erro ao deletar usuário: " + e.getMessage());
            throw new ResponseStatusException(e.getStatusCode());
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> buscarPorEmail(@PathVariable String email) {
        try {
            Usuario usuario = services.buscarPorEmail(email);
            return ResponseEntity.status(200).body(usuario);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatusCode());
        }
    }
}
