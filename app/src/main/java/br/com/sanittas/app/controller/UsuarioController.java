package br.com.sanittas.app.controller;

import br.com.sanittas.app.exception.ValidacaoException;
import br.com.sanittas.app.model.Usuario;
import br.com.sanittas.app.service.UsuarioServices;
import br.com.sanittas.app.service.autenticacao.dto.UsuarioLoginDto;
import br.com.sanittas.app.service.autenticacao.dto.UsuarioTokenDto;
import br.com.sanittas.app.service.usuario.dto.ListaUsuario;
import br.com.sanittas.app.service.usuario.dto.UsuarioCriacaoDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioServices services;

    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenDto> login(@RequestBody UsuarioLoginDto usuarioLoginDto) {
        UsuarioTokenDto usuarioTokenDto = services.autenticar(usuarioLoginDto);
        return ResponseEntity.status(200).body(usuarioTokenDto);
    }
    @GetMapping("/")
    public ResponseEntity<List<ListaUsuario>> listar() {
        try{
            var response = services.listarUsuarios();
            if (!response.isEmpty()){
                return ResponseEntity.status(200).body(response);
            }
            return ResponseEntity.status(204).build();
        }catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Integer id) {
        try{
            var usuario = services.buscar(id);
            return ResponseEntity.status(200).body(usuario);
        }catch (Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/")
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid UsuarioCriacaoDto dados) {
        try{
            services.cadastrar(dados);
            return ResponseEntity.status(201).build();
        }catch (Exception e){
            return ResponseEntity.status(400).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Integer id, @RequestBody @Valid Usuario dados) {
        try{
            var usuario = services.atualizar(id,dados);
            return ResponseEntity.status(200).body(usuario);
        }catch (ValidacaoException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id) {
        try{
            services.deletar(id);
            return ResponseEntity.status(200).build();
        }catch (Exception e){
            return ResponseEntity.status(404).build();
        }
    }
}
