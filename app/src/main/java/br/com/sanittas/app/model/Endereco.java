package br.com.sanittas.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidade que representa um endereço associado a um usuário.
 */
@Getter
@Setter
@Entity(name="Endereco")
@Table(name = "endereco")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario; // Associação muitos para um com a entidade Usuario

    @NotBlank
    private String logradouro; // Nome da rua, avenida, etc.

    @NotBlank
    private String numero; // Número do endereço

    private String complemento; // Informações adicionais (opcional)

    @NotBlank
    private String estado; // Estado do endereço

    @NotBlank
    private String cidade; // Cidade do endereço
}