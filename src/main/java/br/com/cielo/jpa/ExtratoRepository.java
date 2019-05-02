package br.com.cielo.jpa;

import br.com.cielo.model.Movimento;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface ExtratoRepository extends PagingAndSortingRepository<Movimento, Long> {

}

