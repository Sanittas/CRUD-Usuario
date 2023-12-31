package br.com.sanittas.app.service.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa as informações de usuário durante operações de atualização.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListaUsuarioAtualizacaoDto {

    /**
     * Identificador único do usuário.
     */
    private Integer id;

    /**
     * Nome do usuário.
     */
    private String nome;

    /**
     * E-mail do usuário.
     */
    private String email;

    /**
     * CPF do usuário.
     */
    private String cpf;

    /**
     * Senha do usuário.
     */
    private String senha;

    /**
     * Telefone do usuário.
     */

    private String telefone;
}
