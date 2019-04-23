package br.com.cielo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.joda.time.DateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
@Entity
public class Movimento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private DateTime dataLancamento;
    private String descricao;
    private Long numero;
    private String situacao;
    private DateTime dataConferencia;
    private String dadosBancarios;
    private BigDecimal valor;
}
