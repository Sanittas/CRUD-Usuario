package br.com.sanittas.app.service.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.hibernate.validator.constraints.br.CPF;

/**
 * DTO para criação de novos usuários.
 */
@Getter
public class UsuarioCriacaoDto {

    /**
     * Nome do usuário.
     */
    @NotBlank
    private String nome;

    /**
     * Endereço de e-mail do usuário.
     */
    @Email
    private String email;

    /**
     * Número de CPF do usuário.
     */
    @CPF
    private String cpf;

    /**
     * Número de celular do usuário (com padrão definido).
     */
    @Pattern(regexp = "^\\(\\d{2}\\)9\\d{4}-\\d{4}$", message = "Telefone inválido")
    private String celular;

    /**
     * Senha do usuário.
     */
    @NotBlank
    private String senha;
}
