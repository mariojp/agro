package io.ucsal.agro.repos;

import io.ucsal.agro.domain.Animal;
import io.ucsal.agro.domain.Inseminacao;
import io.ucsal.agro.domain.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InseminacaoRepository extends JpaRepository<Inseminacao, Integer> {

    Inseminacao findFirstByVaca(Animal animal);

    Inseminacao findFirstByTouro(Animal animal);

    Inseminacao findFirstByVeterinario(Veterinario veterinario);

}
