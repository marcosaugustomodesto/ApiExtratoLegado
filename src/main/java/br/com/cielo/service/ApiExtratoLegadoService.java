package br.com.cielo.service;

import br.com.cielo.jpa.ExtratoRepository;
import br.com.cielo.model.Movimento;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.jni.Thread;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApiExtratoLegadoService {

    private static final Logger log = LoggerFactory.getLogger(ApiExtratoLegadoService.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public static final String FILE_NAME = "c://json//lancamento-conta-legado.json";

    @Autowired
    private ExtratoRepository repository;

    public List<Movimento> retrieveAllMovimentos() {
        List<Movimento> extrato = new ArrayList<>();
        repository.findAll().iterator().forEachRemaining(extrato::add);
        return extrato;
    }

    @Scheduled(fixedRate = 5000)
    public synchronized void importarDadosJSONLegado() {

        log.info("The time is now {}", dateFormat.format(new Date()));

        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonMovimentos = (JSONArray) parser.parse(new FileReader(FILE_NAME));
            List<Movimento> novosMovimentos = new ObjectMapper().readValue(jsonMovimentos.toJSONString(), new TypeReference<List<Movimento>>() {
            });
            List<Movimento> salvarMovimentos = filtrarMovimentosDuplicados(novosMovimentos);
            System.out.println("Movimentos salvos!");
            salvarMovimentos.forEach(repository::save);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public synchronized List<Movimento> filtrarMovimentosDuplicados(List<Movimento> novosMovimentos) {
        List<Movimento> atualMovimentos = retrieveAllMovimentos();
        List<Movimento> salvar = novosMovimentos
                .stream()
                .filter(novo -> !atualMovimentos.contains(novo))
                .map(novo -> {
                            return Movimento.builder()
                                    .dadosBancarios(novo.getDadosBancarios())
                                    .dataConferencia(novo.getDataConferencia())
                                    .dataLancamento(novo.getDataLancamento())
                                    .descricao(novo.getDescricao())
                                    .numero(novo.getNumero())
                                    .id(novo.getId())
                                    .valor(novo.getValor()).build();
                        }
                ).collect(Collectors.toList());
        return salvar;
    }

/*
    public synchronized List<Movimento> filtrarMovimentosDuplicados(List<Movimento> novosMovimentos) {
        List<Movimento> atualMovimentos = retrieveAllMovimentos();
        List<Movimento> salvar = novosMovimentos;
        novosMovimentos.forEach(novo -> {
            atualMovimentos.forEach(mov -> {
                if(mov.equals(novo)) {
                    salvar.remove(novo);
                }
            });
        });
        return salvar;
    }

 */
}