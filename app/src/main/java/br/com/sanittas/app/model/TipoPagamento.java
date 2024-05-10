package br.com.sanittas.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tipo_pagamento")
public class TipoPagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    private String descricao;
    @NotBlank
    private String tipo;
//    @OneToMany(mappedBy = "tipoPagamento", orphanRemoval = true)
//    private List<Pagamento> pagamentos = new ArrayList<>();
}
