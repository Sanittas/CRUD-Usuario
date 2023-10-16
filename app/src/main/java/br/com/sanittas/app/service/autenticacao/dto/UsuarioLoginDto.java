package br.com.sanittas.app.service.autenticacao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UsuarioLoginDto{
    private String email;
    private String senha;
}


