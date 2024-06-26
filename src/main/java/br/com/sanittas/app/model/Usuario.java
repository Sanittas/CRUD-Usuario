package br.com.sanittas.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import org.hibernate.validator.constraints.br.CPF;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Usuario")
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Identificador único do usuário
    @NotBlank
    private String nome; // Nome do usuário
    @Pattern(regexp = "^\\(\\d{2}\\)9\\d{4}-\\d{4}$", message = "O número de telefone deve estar no " +
            "formato (11)99999-9999")
    private String telefone; // Número de telefone do usuário
    @Email
    private String email; // Endereço de e-mail do usuário
    @CPF
    private String cpf; // Número de CPF do usuário
    @NotBlank
    private String senha; // Senha do usuário
    @JsonIgnore
    @OneToMany(mappedBy = "usuario", orphanRemoval = true)
    private List<EnderecoUsuario> enderecoUsuarios = new ArrayList<>(); // Lista de endereços associados ao usuário
}
