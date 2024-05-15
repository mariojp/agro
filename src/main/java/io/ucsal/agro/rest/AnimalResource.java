package io.ucsal.agro.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.ucsal.agro.model.AnimalDTO;
import io.ucsal.agro.service.AnimalService;
import io.ucsal.agro.util.ReferencedException;
import io.ucsal.agro.util.ReferencedWarning;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/animals", produces = MediaType.APPLICATION_JSON_VALUE)
public class AnimalResource {

    private final AnimalService animalService;

    public AnimalResource(final AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    public ResponseEntity<List<AnimalDTO>> getAllAnimals() {
        return ResponseEntity.ok(animalService.findAll());
    }

    @GetMapping("/{animalId}")
    public ResponseEntity<AnimalDTO> getAnimal(
            @PathVariable(name = "animalId") final Integer animalId) {
        return ResponseEntity.ok(animalService.get(animalId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createAnimal(@RequestBody @Valid final AnimalDTO animalDTO) {
        final Integer createdAnimalId = animalService.create(animalDTO);
        return new ResponseEntity<>(createdAnimalId, HttpStatus.CREATED);
    }

    @PutMapping("/{animalId}")
    public ResponseEntity<Integer> updateAnimal(
            @PathVariable(name = "animalId") final Integer animalId,
            @RequestBody @Valid final AnimalDTO animalDTO) {
        animalService.update(animalId, animalDTO);
        return ResponseEntity.ok(animalId);
    }

    @DeleteMapping("/{animalId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAnimal(
            @PathVariable(name = "animalId") final Integer animalId) {
        final ReferencedWarning referencedWarning = animalService.getReferencedWarning(animalId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        animalService.delete(animalId);
        return ResponseEntity.noContent().build();
    }

}
