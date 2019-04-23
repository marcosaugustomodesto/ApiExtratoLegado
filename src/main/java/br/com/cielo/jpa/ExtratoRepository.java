package br.com.cielo.jpa;

import br.com.cielo.model.Movimento;
import org.springframework.data.repository.CrudRepository;


public interface ExtratoRepository extends CrudRepository<Movimento, Long> {

}

