package br.com.sanittas.app.service;

import br.com.sanittas.app.api.configuration.security.jwt.GerenciadorTokenJwt;
import br.com.sanittas.app.exception.ValidacaoException;
import br.com.sanittas.app.model.Endereco;
import br.com.sanittas.app.model.Usuario;
import br.com.sanittas.app.repository.UsuarioRepository;
import br.com.sanittas.app.service.autenticacao.dto.UsuarioLoginDto;
import br.com.sanittas.app.service.autenticacao.dto.UsuarioTokenDto;
import br.com.sanittas.app.service.endereco.dto.ListaEndereco;
import br.com.sanittas.app.service.usuario.dto.ListaUsuario;
import br.com.sanittas.app.service.usuario.dto.ListaUsuarioAtualizacao;
import br.com.sanittas.app.service.usuario.dto.UsuarioCriacaoDto;
import br.com.sanittas.app.service.usuario.dto.UsuarioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServices {
    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;
    @Autowired
    private AuthenticationManager authenticationManager;

    public List<ListaUsuario> listarUsuarios() {
        var usuarios = repository.findAll();
        List<ListaUsuario> listaUsuarios = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            List<ListaEndereco> listaEnderecos = new ArrayList<>();
            criarDtoEndereco(usuario, listaEnderecos);
            criarDtoUsuarios(usuario, listaEnderecos, listaUsuarios);
        }
        return listaUsuarios;
    }

    private static void criarDtoUsuarios(Usuario usuario, List<ListaEndereco> listaEnderecos, List<ListaUsuario> listaUsuarios) {
        var usuarioDto = new ListaUsuario(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
//                    usuario.getCelular(),
                usuario.getSenha(),
                listaEnderecos
        );
        listaUsuarios.add(usuarioDto);
    }

    private static void criarDtoEndereco(Usuario usuario, List<ListaEndereco> listaEnderecos) {
        for (Endereco endereco : usuario.getEnderecos()) {
            var enderecoDto = new ListaEndereco(
                    endereco.getId(),
                    endereco.getLogradouro(),
                    endereco.getNumero(),
                    endereco.getComplemento(),
                    endereco.getEstado(),
                    endereco.getCidade()
            );
            listaEnderecos.add(enderecoDto);
        }
    }

    public void cadastrar(UsuarioCriacaoDto usuarioCriacaoDto) {
        final Usuario novoUsuario = UsuarioMapper.of(usuarioCriacaoDto);

        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaCriptografada);

        repository.save(novoUsuario);
    }

    public UsuarioTokenDto autenticar(UsuarioLoginDto usuarioLoginDto) {
        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                usuarioLoginDto.email(), usuarioLoginDto.senha());

        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        Usuario usuarioAutenticado =
                repository.findByEmail(usuarioLoginDto.email())
                        .orElseThrow(
                                () -> new ResponseStatusException(404, "Email do usuário não cadastrado", null)
                        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String jwtToken = gerenciadorTokenJwt.generateToken(authentication);

        return UsuarioMapper.of(usuarioAutenticado, jwtToken);
    }


    public ListaUsuarioAtualizacao atualizar(Integer id, Usuario dados) {
        var usuario = repository.findById(id);
        if (usuario.isPresent()) {
            usuario.get().setNome(dados.getNome());
            usuario.get().setEmail(dados.getEmail());
            usuario.get().setCpf(dados.getCpf());
//            usuario.get().setCelular(dados.getCelular());
            usuario.get().setSenha(dados.getSenha());
            ListaUsuarioAtualizacao usuarioDto = new ListaUsuarioAtualizacao(
                    usuario.get().getId(),
                    usuario.get().getNome(),
                    usuario.get().getEmail(),
                    usuario.get().getCpf(),
//                    usuario.get().getCelular(),
                    usuario.get().getSenha()
            );
            repository.save(usuario.get());
            return usuarioDto;
        }
        return null;
    }

    public void deletar(Integer id) {
        if (!repository.existsById(id)) {
            throw new ValidacaoException("Usuário não existe!");
        }
        repository.deleteById(id);
    }

    public ListaUsuario buscar(Integer id) {
        var usuario = repository.findById(id);
        if (usuario.isEmpty()) {
            throw new ValidacaoException("Usuário não existe!");
        }
        List<ListaEndereco> listaEnderecos = new ArrayList<>();
        criarDtoEndereco(usuario.get(), listaEnderecos);
        ListaUsuario usuarioDto = criarDtoUsuario(usuario, listaEnderecos);
        return usuarioDto;
    }

    private static ListaUsuario criarDtoUsuario(Optional<Usuario> usuario, List<ListaEndereco> listaEnderecos) {
        ListaUsuario usuarioDto = new ListaUsuario(
                usuario.get().getId(),
                usuario.get().getNome(),
                usuario.get().getEmail(),
                usuario.get().getCpf(),
//                    usuario.get().getCelular(),
                usuario.get().getSenha(),
                listaEnderecos
        );
        return usuarioDto;
    }

}
