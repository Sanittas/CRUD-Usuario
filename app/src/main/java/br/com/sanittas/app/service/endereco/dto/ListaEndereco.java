package br.com.sanittas.app.service.endereco.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa as informações de endereço em uma lista.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListaEndereco {

        /**
         * Identificador único do endereço.
         */
        private Long id;

        /**
         * Logradouro do endereço.
         */
        private String logradouro;

        /**
         * Número do endereço.
         */
        private String numero;

        /**
         * Complemento do endereço.
         */
        private String complemento;

        /**
         * Estado do endereço.
         */
        private String estado;

        /**
         * Cidade do endereço.
         */
        private String cidade;
}
