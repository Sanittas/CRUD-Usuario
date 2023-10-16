package br.com.sanittas.app.service.usuario.dto;

import br.com.sanittas.app.service.endereco.dto.ListaEndereco;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@Getter
@Setter
public class ListaUsuario{
        private Integer id;
        private String nome;
        private String email;
        private String cpf;
        private String senha;
        private List<ListaEndereco> enderecos;
}
