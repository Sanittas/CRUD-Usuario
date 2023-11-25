package br.com.sanittas.app.service;

import br.com.sanittas.app.model.Endereco;
import br.com.sanittas.app.model.Usuario;
import br.com.sanittas.app.repository.EnderecoRepository;
import br.com.sanittas.app.repository.UsuarioRepository;
import br.com.sanittas.app.service.endereco.dto.EnderecoCriacaoDto;
import br.com.sanittas.app.service.endereco.dto.EnderecoMapper;
import br.com.sanittas.app.service.endereco.dto.ListaEndereco;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EnderecoServices {

    @Autowired
    private EnderecoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<ListaEndereco> listarEnderecosPorUsuario(Integer idUsuario) {
        try {
            log.info("Listando endereços para o usuário com ID: {}", idUsuario);

            Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
            List<ListaEndereco> enderecos = new ArrayList<>();

            if (usuario.isPresent()) {
                for (Endereco endereco : usuario.get().getEnderecos()) {
                    var enderecoDto = new ListaEndereco(
                            endereco.getId(),
                            endereco.getLogradouro(),
                            endereco.getNumero(),
                            endereco.getComplemento(),
                            endereco.getEstado(),
                            endereco.getCidade()
                    );
                    enderecos.add(enderecoDto);
                }
                return enderecos;
            } else {
                log.error("Usuário com ID {} não encontrado", idUsuario);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Erro ao listar endereços para o usuário com ID {}: {}", idUsuario, e.getMessage());
            throw e;
        }
    }

    public void cadastrarEnderecoUsuario(EnderecoCriacaoDto enderecoCriacaoDto, Integer usuario_id) {
        try {
            log.info("Cadastrando endereço para o usuário com ID: {}", usuario_id);

            var endereco = EnderecoMapper.of(enderecoCriacaoDto);
            var usuario = usuarioRepository.findById(usuario_id);

            if (usuario.isPresent()) {
                endereco.setUsuario(usuario.get());
                repository.save(endereco);
            } else {
                log.error("Usuário com ID {} não encontrado", usuario_id);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Erro ao cadastrar endereço para o usuário com ID {}: {}", usuario_id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public ListaEndereco atualizar(EnderecoCriacaoDto enderecoCriacaoDto, Long id) {
        try {
            log.info("Atualizando endereço com ID: {}", id);
            var endereco = repository.findById(id);
            if (endereco.isPresent()) {
                endereco.get().setLogradouro(enderecoCriacaoDto.getLogradouro());
                endereco.get().setNumero(enderecoCriacaoDto.getNumero());
                endereco.get().setComplemento(enderecoCriacaoDto.getComplemento());
                endereco.get().setEstado(enderecoCriacaoDto.getEstado());
                endereco.get().setCidade(enderecoCriacaoDto.getCidade());
                repository.save(endereco.get());

                return new ListaEndereco(
                        endereco.get().getId(),
                        endereco.get().getLogradouro(),
                        endereco.get().getNumero(),
                        endereco.get().getComplemento(),
                        endereco.get().getEstado(),
                        endereco.get().getCidade()
                );
            } else {
                log.error("Endereço com ID {} não encontrado", id);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Erro ao atualizar endereço com ID {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void deletarEndereco(Long id) {
        try {
            log.info("Deletando endereço com ID: {}", id);

            if (!repository.existsById(id)) {
                log.error("Endereço com ID {} não encontrado", id);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            repository.deleteById(id);
        } catch (Exception e) {
            log.error("Erro ao deletar endereço com ID {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
