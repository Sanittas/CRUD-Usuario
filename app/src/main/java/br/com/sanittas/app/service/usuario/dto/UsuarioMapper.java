package br.com.sanittas.app.service.usuario.dto;

import br.com.sanittas.app.model.Usuario;
import br.com.sanittas.app.service.autenticacao.dto.UsuarioTokenDto;
import br.com.sanittas.app.service.usuario.dto.UsuarioCriacaoDto;

/**
 * Classe responsável por mapear objetos entre a entidade Usuario e os DTOs relacionados.
 */
public class UsuarioMapper {

    /**
     * Converte um objeto UsuarioCriacaoDto para a entidade Usuario.
     *
     * @param usuarioCriacaoDto DTO contendo informações para criar um novo usuário.
     * @return Entidade Usuario criada a partir do DTO.
     */
    public static Usuario of(UsuarioCriacaoDto usuarioCriacaoDto) {
        Usuario usuario = new Usuario();

        usuario.setNome(usuarioCriacaoDto.getNome());
        usuario.setEmail(usuarioCriacaoDto.getEmail());
        usuario.setSenha(usuarioCriacaoDto.getSenha());
        usuario.setCpf(usuarioCriacaoDto.getCpf());
        usuario.setTelefone(usuarioCriacaoDto.getTelefone());
//        usuario.setCelular(usuarioCriacaoDto.getCelular());

        return usuario;
    }

    /**
     * Converte um objeto Usuario e um token para o DTO UsuarioTokenDto.
     *
     * @param usuario Entidade Usuario.
     * @param token   Token associado ao usuário.
     * @return DTO UsuarioTokenDto criado a partir da entidade Usuario e do token.
     */
    public static UsuarioTokenDto of(Usuario usuario, String token) {
        UsuarioTokenDto usuarioTokenDto = new UsuarioTokenDto();

        usuarioTokenDto.setUserId(usuario.getId());
        usuarioTokenDto.setNome(usuario.getNome());
        usuarioTokenDto.setEmail(usuario.getEmail());
        usuarioTokenDto.setToken(token);

        return usuarioTokenDto;
    }
}
