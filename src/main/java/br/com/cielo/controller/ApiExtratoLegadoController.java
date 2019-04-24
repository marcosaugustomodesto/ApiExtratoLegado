package br.com.cielo.controller;

import br.com.cielo.model.Movimento;
import br.com.cielo.service.ApiExtratoLegadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class ApiExtratoLegadoController {


    @Autowired
    private ApiExtratoLegadoService apiExtratoLegadoService;


    @GetMapping("/extrato/all")
    public List<Movimento> retrieveAllMovimentos() {
        return apiExtratoLegadoService.retrieveAllMovimentos();
    }

}
