package br.com.sanittas.app.service.usuario.dto;

public record ListaUsuarioAtualizacao(
        Integer id,
        String nome,
        String email,
        String cpf,
        String senha
) {
}
