package br.com.sanittas.app.service.autenticacao.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) para representar informações do usuário e o token associado.
 */
@Getter
@Setter
public class UsuarioTokenDto {
    private Integer userId; // ID do usuário
    private String nome;     // Nome do usuário
    private String email;    // Endereço de e-mail do usuário
    private String token;    // Token associado ao usuário após autenticação
}
