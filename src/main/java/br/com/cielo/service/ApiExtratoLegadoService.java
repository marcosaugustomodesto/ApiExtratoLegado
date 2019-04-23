package br.com.cielo.service;

import br.com.cielo.jpa.ExtratoRepository;
import br.com.cielo.model.Movimento;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ApiExtratoLegadoService {

    private static List<Movimento> extrato = new ArrayList<>();

    private static final Logger log = LoggerFactory.getLogger(ApiExtratoLegadoService.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");



    @Autowired
    private ExtratoRepository repository;

    static {
        Movimento movimento1 = Movimento.builder()
                .dadosBancarios("BANCO ABCD SA AG 12 CC 0001231234")
                .dataConferencia(new DateTime())
                .dataLancamento(new DateTime())
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
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Movimento>> typeReference = new TypeReference<List<Movimento>>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/json/lancamento-conta-legado.json");
        try {
            List<Movimento> movimentos = mapper.readValue(inputStream,typeReference);
            movimentos.forEach(movimento -> repository.save(movimento));
            System.out.println("Movimentos Saved!");
        } catch (IOException e){
            System.out.println("Unable to save movimentos: " + e.getMessage());
        }

    }

}