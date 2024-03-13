package br.com.sanittas.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="endereco_usuario")
public class EnderecoUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_end_usuario")
    private Integer id;
    @ManyToOne
    private Usuario usuario; // Associação muitos para um com a entidade Usuario

    @NotBlank
    private String logradouro;

    @NotBlank
    private String numero;

    private String complemento;

    @NotBlank
    private String estado;

    @NotBlank
    private String cidade;
}