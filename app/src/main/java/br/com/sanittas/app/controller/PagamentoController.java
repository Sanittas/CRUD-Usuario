package br.com.sanittas.app.controller;


import br.com.sanittas.app.service.PagamentoServices;
import br.com.sanittas.app.service.pagamento.dto.PagamentoReponse;
import br.com.sanittas.app.service.pagamento.dto.PagamentoRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios/pagamentos")
@Slf4j
@CrossOrigin(origins = "*")
public class PagamentoController {

    @Autowired
    private PagamentoServices pagamentoService;

    @PostMapping("/criar-pagamento")
    public ResponseEntity<PagamentoReponse> criarPagamento(@RequestBody @Valid PagamentoRequest pagamentoRequest) {
        try {
            log.info("Recebida solicitação de criação de pagamento");
            var response = pagamentoService.criarPagamento(pagamentoRequest);
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            log.error("Erro ao criar pagamento: {}", e.getMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }


}
