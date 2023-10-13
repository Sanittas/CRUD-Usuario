package br.com.sanittas.app.service.usuario.dto;

import br.com.sanittas.app.model.Endereco;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

public class UsuarioCriacaoDto {
    @NotBlank
    private String nome;
    @Email
    private String email;
    @CPF
    private String cpf;
    @Pattern(regexp = "^\\(\\d{2}\\)9\\d{4}-\\d{4}$", message = "Telefone inválido")
    private String celular;
    @NotBlank
    private String senha;

    public String getNome() {
        return nome;
    }
    public String getEmail() {
        return email;
    }
    public String getCpf() {
        return cpf;
    }
    public String getCelular() {
        return celular;
    }
    public String getSenha() {
        return senha;
    }
}
