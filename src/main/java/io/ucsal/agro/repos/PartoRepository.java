package io.ucsal.agro.repos;

import io.ucsal.agro.domain.Inseminacao;
import io.ucsal.agro.domain.Parto;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PartoRepository extends JpaRepository<Parto, Integer> {

    Parto findFirstByInseminacao(Inseminacao inseminacao);

}
