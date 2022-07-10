package com.example.computadores.domain;

import com.example.computadores.errorhandling.ApiMenssages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Computador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = ApiMenssages.ERRO_VAZIO)
    String modelo;
    String marca;
    String preco;
    String imagem;
    String descricao;
    Boolean deletd;

}
