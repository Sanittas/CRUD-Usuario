package br.com.sanittas.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import org.hibernate.validator.constraints.br.CPF;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa um usuário do sistema.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "Usuario")
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Identificador único do usuário

    @NotBlank
    private String nome; // Nome do usuário

    @Email
    private String email; // Endereço de e-mail do usuário

    @CPF
    private String cpf; // Número de CPF do usuário

    // Outros campos que estão comentados

    @NotBlank
    private String senha; // Senha do usuário

    @OneToMany(mappedBy = "usuario", orphanRemoval = true)
    private List<Endereco> enderecos = new ArrayList<>(); // Lista de endereços associados ao usuário
}
