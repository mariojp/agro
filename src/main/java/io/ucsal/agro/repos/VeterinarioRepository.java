package io.ucsal.agro.repos;

import io.ucsal.agro.domain.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VeterinarioRepository extends JpaRepository<Veterinario, Integer> {
}
