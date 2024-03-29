package br.com.sanittas.app.controller;

import br.com.sanittas.app.model.Usuario;
import br.com.sanittas.app.service.EmailServices;
import br.com.sanittas.app.service.UsuarioServices;
import br.com.sanittas.app.service.dto.LoginDtoRequest;
import br.com.sanittas.app.service.dto.LoginDtoResponse;
import br.com.sanittas.app.service.usuario.dto.NovaSenhaDto;
import br.com.sanittas.app.service.usuario.dto.UsuarioCriacaoDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Slf4j
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioServices services; // Serviços relacionados a usuários
    @Autowired
    private EmailServices emailServices; // Serviços relacionados a e-mails

    @PostMapping("/login")
    public ResponseEntity<LoginDtoResponse> login(@RequestBody @Valid LoginDtoRequest loginDto) {
        try {
            log.info("Recebida solicitação de login para usuário com e-mail: {}", loginDto.email());
            Usuario usuario = services.login(loginDto);
            log.info("Login efetuado com sucesso para usuário: {}", usuario.getNome());
            return ResponseEntity.status(200).body(new LoginDtoResponse(usuario.getNome(), usuario.getId(), usuario.getEmail()));
        } catch (ResponseStatusException e) {
            log.error("Erro ao efetuar login: {}", e.getMessage());
            throw new ResponseStatusException(e.getStatusCode());
        }
    }
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
     * Cadastra um novo usuário.
     *
     * @param dados O DTO contendo as informações do novo usuário.
     * @return Uma ResponseEntity indicando o sucesso ou falha da operação.
     */
    @PostMapping("/cadastrar/")
    public ResponseEntity<Usuario> cadastrar(@RequestBody @Valid UsuarioCriacaoDto dados) {
        try {
            Usuario response = services.cadastrar(dados);
            return ResponseEntity.status(201).body(response); // Criado com sucesso
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

    /**
     * Envia um e-mail com um token para redefinição de senha.
     *
     * @param email O endereço de e-mail do usuário.
     * @return Uma ResponseEntity indicando o sucesso ou falha da operação.
     */
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
}
