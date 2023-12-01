package br.com.sanittas.app.service.usuario.dto;

import br.com.sanittas.app.service.endereco.dto.ListaEndereco;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO que representa as informações de usuário em uma lista.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListaUsuarioDto {

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

        /**
         * Lista de endereços associados ao usuário.
         */
        private List<ListaEndereco> enderecos;
}
