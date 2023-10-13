package br.com.sanittas.app.service.autenticacao.dto;

public record UsuarioLoginDto(
        String email,
        String senha
) {
}
