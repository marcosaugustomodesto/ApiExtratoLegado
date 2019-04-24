package br.com.cielo.service;

import br.com.cielo.jpa.ExtratoRepository;
import br.com.cielo.model.Movimento;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private static List<Movimento> extrato = new ArrayList<>();

    private static final Logger log = LoggerFactory.getLogger(ApiExtratoLegadoService.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public static final String FILE_NAME = "c://json//lancamento-conta-legado.json";

    @Autowired
    private ExtratoRepository repository;

    static {
        Movimento movimento1 = Movimento.builder()
                .dadosBancarios("BANCO ABCD SA AG 12 CC 0001231234")
                .dataConferencia(new Date())
                .dataLancamento(new Date())
                .descricao("REGULAR").numero(672108074000l).situacao("PAGO").valor(new BigDecimal("28714")).build();

        extrato.add(movimento1);

    }

    public List<Movimento> retrieveAllMovimentos() {
        repository.findAll().iterator().forEachRemaining(extrato::add);
        return extrato;
    }

    @Scheduled(fixedRate = 5000)
    public void importarDadosJSONLegado() {
        log.info("The time is now {}", dateFormat.format(new Date()));

        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonMovimentos = (JSONArray) parser.parse(new FileReader(FILE_NAME));
            List<Movimento> novosMovimentos = new ObjectMapper().readValue(jsonMovimentos.toJSONString(), new TypeReference<List<Movimento>>() {});
            List<Movimento> salvarMovimentos = filtrarMovimentosDuplicados(novosMovimentos);
            System.out.println("Movimentos salvos!");
            salvarMovimentos.forEach(repository::save);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public List<Movimento> filtrarMovimentosDuplicados(List<Movimento> novosMovimentos) {
        List<Movimento> atualMovimentos = retrieveAllMovimentos();
        return novosMovimentos.stream().filter(mov -> !atualMovimentos.contains(mov)).collect(Collectors.toList());
    }
}