package br.com.sanittas.app.service.dto;

public record LoginDtoRequest(
        String email,
        String senha
) {
}
