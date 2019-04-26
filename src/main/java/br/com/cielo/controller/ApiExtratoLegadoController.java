package br.com.cielo.controller;

import br.com.cielo.model.Movimento;
import br.com.cielo.service.ApiExtratoLegadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class ApiExtratoLegadoController {


    @Autowired
    private ApiExtratoLegadoService apiExtratoLegadoService;

    @RequestMapping(value = "/extrato/all", method = RequestMethod.GET,  produces = { "application/json", "application/xml" })
    public List<Movimento> retrieveAllMovimentos() {
        return apiExtratoLegadoService.retrieveAllMovimentos();
    }

}
