package io.ucsal.agro.repos;

import io.ucsal.agro.domain.Animal;
import io.ucsal.agro.domain.Parto;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AnimalRepository extends JpaRepository<Animal, Integer> {

    Animal findFirstByParto(Parto parto);

    Animal findFirstByMaeAndAnimalIdNot(Animal animal, final Integer animalId);

    Animal findFirstByPaiAndAnimalIdNot(Animal animal, final Integer animalId);

}
