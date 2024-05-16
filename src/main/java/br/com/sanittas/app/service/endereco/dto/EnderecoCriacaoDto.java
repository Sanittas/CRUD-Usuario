package br.com.sanittas.app.service.endereco.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) para representar informações necessárias para a criação de um endereço.
 */
@Getter
@Setter
public class EnderecoCriacaoDto {
    @NotBlank
    private String logradouro;   // Logradouro do endereço
    private String numero;       // Número do endereço
    private String complemento;  // Complemento do endereço
    @NotBlank
    private String cidade;       // Cidade do endereço
    @NotBlank
    private String estado;       // Estado do endereço
}
