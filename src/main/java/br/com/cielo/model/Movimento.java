package br.com.cielo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import static java.util.Objects.isNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Movimento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt_BR")
    private Date dataLancamento;

    private String descricao;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Long numero;

    private String situacao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt_BR")
    private Date dataConferencia;

    private String dadosBancarios;

    private Double valor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movimento)) return false;
        Movimento movimento = (Movimento) o;
        return ((isNull(dataLancamento) && isNull(movimento.dataLancamento)) ||
                (Objects.equals(dataLancamento.getTime(), movimento.dataLancamento.getTime()))) &&
                Objects.equals(descricao, movimento.descricao) &&
                Objects.equals(numero, movimento.numero) &&
                Objects.equals(situacao, movimento.situacao) &&
                (isNull(dataConferencia) && isNull(movimento.dataConferencia)) ||
                (Objects.equals(dataConferencia.getTime(), movimento.dataConferencia.getTime())) &&
                        Objects.equals(dadosBancarios, movimento.dadosBancarios) &&
                        Objects.equals(valor, movimento.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataLancamento, descricao, numero, situacao, dataConferencia, dadosBancarios, valor);
    }
}
