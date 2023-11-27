package br.com.sanittas.app.service.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

/**
 * DTO para criação de novos usuários.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
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
    private String telefone;

    /**
     * Senha do usuário.
     */
    @Size(min = 8, max = 20)
    private String senha;


}
