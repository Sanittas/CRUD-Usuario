package br.com.sanittas.app.service.pagamento.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoReponse {
    private Long id;
    private String status;
    private String qrCode;
    private String linkPagamento;
    private BigDecimal valor;


}
