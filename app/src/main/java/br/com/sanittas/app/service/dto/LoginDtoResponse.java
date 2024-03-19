package br.com.sanittas.app.service.dto;

public record LoginDtoResponse(
        String nome,
        Integer id,
        String email
) {
}
