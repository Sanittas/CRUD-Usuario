package br.com.sanittas.app.service;

import br.com.sanittas.app.exception.ValidacaoException;
import br.com.sanittas.app.model.Endereco;
import br.com.sanittas.app.repository.EnderecoRepository;
import br.com.sanittas.app.repository.UsuarioRepository;
import br.com.sanittas.app.service.endereco.dto.EnderecoCriacaoDto;
import br.com.sanittas.app.service.endereco.dto.EnderecoMapper;
import br.com.sanittas.app.service.endereco.dto.ListaEndereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EnderecoServices {

    @Autowired
    private EnderecoRepository repository;
    @Autowired
    private UsuarioRepository usuarioRepository;


    public List<ListaEndereco> listarEnderecosPorUsuario(Integer idUsuario) {
        var usuario = usuarioRepository.findById(idUsuario);
        List<ListaEndereco> enderecos = new ArrayList<>();
        if (usuario.isPresent()){
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
        }
        throw new ValidacaoException("Usuário não encontrado");
    }

    public void cadastrarEnderecoUsuario(EnderecoCriacaoDto enderecoCriacaoDto,Integer usuario_id) {
        var endereco = EnderecoMapper.of(enderecoCriacaoDto);
        var usuario = usuarioRepository.findById(usuario_id);
            endereco.setUsuario(usuario.get());
            repository.save(endereco);
    }

    public ListaEndereco atualizar(EnderecoCriacaoDto enderecoCriacaoDto, Long id) {
        var endereco = repository.findById(id);
        if (endereco.isPresent()){
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
        }
        throw new ValidacaoException("Endereço não encontrado");
    }



    public void deletarEndereco(Long id) {
        if (!repository.existsById(id)) {
            throw new ValidacaoException("Endereço não existe!");
        }
        repository.deleteById(id);
    }



}
