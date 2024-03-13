package br.com.sanittas.app.service.usuario.dto;

import br.com.sanittas.app.model.Usuario;

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
}
