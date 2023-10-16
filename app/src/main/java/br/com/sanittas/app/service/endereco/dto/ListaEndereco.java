package br.com.sanittas.app.service.endereco.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ListaEndereco {

        private Long id;
        private String logradouro;
        private String numero;
        private String complemento;
        private String estado;
        private String cidade;


}
