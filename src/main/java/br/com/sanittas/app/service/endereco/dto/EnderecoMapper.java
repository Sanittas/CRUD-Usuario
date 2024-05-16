package br.com.sanittas.app.service.endereco.dto;

import br.com.sanittas.app.model.EnderecoUsuario;

/**
 * Classe utilitária para mapear um objeto do tipo EnderecoCriacaoDto para um objeto do tipo Endereco.
 */
public class EnderecoMapper {

    /**
     * Converte um objeto do tipo EnderecoCriacaoDto para um objeto do tipo Endereco.
     *
     * @param enderecoCriacaoDto Objeto contendo informações para a criação de um endereço.
     * @return Um objeto do tipo Endereco.
     */
    public static EnderecoUsuario of(EnderecoCriacaoDto enderecoCriacaoDto) {
        EnderecoUsuario enderecoUsuario = new EnderecoUsuario();

        enderecoUsuario.setLogradouro(enderecoCriacaoDto.getLogradouro());
        enderecoUsuario.setNumero(enderecoCriacaoDto.getNumero());
        enderecoUsuario.setComplemento(enderecoCriacaoDto.getComplemento());
        enderecoUsuario.setCidade(enderecoCriacaoDto.getCidade());
        enderecoUsuario.setEstado(enderecoCriacaoDto.getEstado());

        return enderecoUsuario;
    }
}
