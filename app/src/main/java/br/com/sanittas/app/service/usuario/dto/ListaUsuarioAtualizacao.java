
package br.com.sanittas.app.service.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ListaUsuarioAtualizacao{
    private Integer id;
    private String nome;
    private String email;
    private String cpf;
    private String senha;
}
