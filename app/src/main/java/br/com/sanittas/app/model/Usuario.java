package br.com.sanittas.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.util.ArrayList;
import java.util.List;

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
    private Integer id;
    @NotBlank
    private String nome;
    @Email
    private String email;
    @CPF
    private String cpf;
    //    @Pattern(regexp = "^\\(\\d{2}\\)9\\d{4}-\\d{4}$", message = "Telefone inválido")
//    private String celular;
    @NotBlank
    private String senha;
    @OneToMany(mappedBy = "usuario",orphanRemoval = true)
    private List<Endereco> enderecos = new ArrayList<>();


}
