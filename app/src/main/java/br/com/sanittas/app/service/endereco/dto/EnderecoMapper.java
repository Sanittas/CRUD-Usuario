package br.com.sanittas.app.service.endereco.dto;

import br.com.sanittas.app.model.Endereco;

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
    public static Endereco of(EnderecoCriacaoDto enderecoCriacaoDto) {
        Endereco endereco = new Endereco();

        endereco.setLogradouro(enderecoCriacaoDto.getLogradouro());
        endereco.setNumero(enderecoCriacaoDto.getNumero());
        endereco.setComplemento(enderecoCriacaoDto.getComplemento());
        endereco.setCidade(enderecoCriacaoDto.getCidade());
        endereco.setEstado(enderecoCriacaoDto.getEstado());

        return endereco;
    }
}
