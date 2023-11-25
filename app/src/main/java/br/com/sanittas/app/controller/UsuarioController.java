package br.com.sanittas.app.controller;

import br.com.sanittas.app.model.Usuario;
import br.com.sanittas.app.service.EmailServices;
import br.com.sanittas.app.service.UsuarioServices;
import br.com.sanittas.app.service.autenticacao.dto.UsuarioLoginDto;
import br.com.sanittas.app.service.autenticacao.dto.UsuarioTokenDto;
import br.com.sanittas.app.service.usuario.dto.ListaUsuario;
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

/**
 * Controlador para operações relacionadas a usuários.
 */
@RestController
@SecurityRequirement(name = "bearer-key") // Requisito de segurança para autenticação JWT
@RequestMapping("/usuarios")
@Slf4j
public class UsuarioController {

    @Autowired
    private UsuarioServices services; // Serviços relacionados a usuários
    @Autowired
    private EmailServices emailServices; // Serviços relacionados a e-mails

    /**
     * Autentica um usuário e retorna um token JWT.
     *
     * @param usuarioLoginDto DTO contendo as informações de login do usuário.
     * @return Uma ResponseEntity contendo o token de autenticação ou uma resposta de falha.
     */
    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenDto> login(@RequestBody UsuarioLoginDto usuarioLoginDto) {
        UsuarioTokenDto usuarioTokenDto = services.autenticar(usuarioLoginDto);
        return ResponseEntity.status(200).body(usuarioTokenDto);
    }

    /**
     * Lista todos os usuários.
     *
     * @return Uma ResponseEntity contendo a lista de usuários ou uma resposta vazia.
     */
    @GetMapping("/")
    public ResponseEntity<List<ListaUsuario>> listar() {
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
    public ResponseEntity<?> buscar(@PathVariable Integer id) {
        try {
            var usuario = services.buscar(id);
            return ResponseEntity.status(200).body(usuario);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    /**
     * Cadastra um novo usuário.
     *
     * @param dados O DTO contendo as informações do novo usuário.
     * @return Uma ResponseEntity indicando o sucesso ou falha da operação.
     */
    @PostMapping("/cadastrar/")
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid UsuarioCriacaoDto dados) {
        try {
            services.cadastrar(dados);
            return ResponseEntity.status(201).build(); // Criado com sucesso
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
    public ResponseEntity<?> atualizar(@PathVariable Integer id, @RequestBody @Valid Usuario dados) {
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
    @PostMapping("/esqueci-senha")
    public ResponseEntity<?> esqueciASenha(@RequestParam String email) {
        try {
            String token = services.generateToken(email);
            emailServices.enviarEmailComToken(email, token);
            return ResponseEntity.status(200).build();
        } catch (Exception e) {
            log.info(e.getLocalizedMessage());
            return ResponseEntity.status(400).body(e.getLocalizedMessage());
        }
    }

    /**
     * Valida um token para redefinição de senha.
     *
     * @param token O token a ser validado.
     * @return Uma ResponseEntity indicando o sucesso ou falha da operação.
     */
    @GetMapping("/validarToken/{token}")
    public ResponseEntity<?> validarToken(@PathVariable String token) {
        try {
            services.validarToken(token);
            return ResponseEntity.status(200).build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getLocalizedMessage());
        }
    }

    /**
     * Altera a senha de um usuário.
     *
     * @param novaSenhaDto O DTO contendo a nova senha e o token de validação.
     * @return Uma ResponseEntity indicando o sucesso ou falha da operação.
     */
    @PutMapping("/alterar-senha")
    public ResponseEntity<?> alterarSenha(@RequestBody @Valid NovaSenhaDto novaSenhaDto) {
        try {
            services.alterarSenha(novaSenhaDto);
            return ResponseEntity.status(200).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).build(); // Conflito, token inválido
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getLocalizedMessage());
        }
    }

    /**
     * Deleta um usuário.
     *
     * @param id O ID do usuário a ser deletado.
     * @return Uma ResponseEntity indicando o sucesso ou falha da operação.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id) {
        try {
            services.deletar(id);
            return ResponseEntity.status(200).build();
        } catch (Exception e) {
            return ResponseEntity.status(404).build(); // Não encontrado
        }
    }
}
