package br.com.sanittas.app.controller;

import br.com.sanittas.app.service.EnderecoServices;
import br.com.sanittas.app.service.endereco.dto.EnderecoCriacaoDto;
import br.com.sanittas.app.service.endereco.dto.ListaEndereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoServices usuarioServices;
    @Autowired
    private EnderecoServices enderecoServices;

    @GetMapping("/usuarios/{id_usuario}")
    public ResponseEntity<List<ListaEndereco>> listarEnderecosPorUsuario(@PathVariable Integer id_usuario) {
        try{
            var response = usuarioServices.listarEnderecosPorUsuario(id_usuario);
            if (!response.isEmpty()) {
                return ResponseEntity.status(200).body(response);
            }
            return ResponseEntity.status(204).build();
        }catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(400).build();
        }
    }

    @PostMapping("/usuarios/{usuario_id}")
    public ResponseEntity<Void> cadastrarEnderecoUsuario(@RequestBody EnderecoCriacaoDto endereco, @PathVariable Integer usuario_id) {
        try {
            enderecoServices.cadastrarEnderecoUsuario(endereco,usuario_id);
            return ResponseEntity.status(201).build();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(400).build();
        }
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<ListaEndereco> atualizarEndereco(@RequestBody EnderecoCriacaoDto enderecoCriacaoDto,@PathVariable Long id) {
        try{
            var endereco = enderecoServices.atualizar(enderecoCriacaoDto, id);
            return ResponseEntity.status(200).body(endereco);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(400).build();
        }
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> deletarEnderecoUsuario(@PathVariable Long id) {
        try{
            enderecoServices.deletarEndereco(id);
            return ResponseEntity.status(200).build();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(400).build();
        }
    }

}

