package io.ucsal.agro.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.ucsal.agro.model.VeterinarioDTO;
import io.ucsal.agro.service.VeterinarioService;
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
@RequestMapping(value = "/api/veterinarios", produces = MediaType.APPLICATION_JSON_VALUE)
public class VeterinarioResource {

    private final VeterinarioService veterinarioService;

    public VeterinarioResource(final VeterinarioService veterinarioService) {
        this.veterinarioService = veterinarioService;
    }

    @GetMapping
    public ResponseEntity<List<VeterinarioDTO>> getAllVeterinarios() {
        return ResponseEntity.ok(veterinarioService.findAll());
    }

    @GetMapping("/{veterinarioId}")
    public ResponseEntity<VeterinarioDTO> getVeterinario(
            @PathVariable(name = "veterinarioId") final Integer veterinarioId) {
        return ResponseEntity.ok(veterinarioService.get(veterinarioId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createVeterinario(
            @RequestBody @Valid final VeterinarioDTO veterinarioDTO) {
        final Integer createdVeterinarioId = veterinarioService.create(veterinarioDTO);
        return new ResponseEntity<>(createdVeterinarioId, HttpStatus.CREATED);
    }

    @PutMapping("/{veterinarioId}")
    public ResponseEntity<Integer> updateVeterinario(
            @PathVariable(name = "veterinarioId") final Integer veterinarioId,
            @RequestBody @Valid final VeterinarioDTO veterinarioDTO) {
        veterinarioService.update(veterinarioId, veterinarioDTO);
        return ResponseEntity.ok(veterinarioId);
    }

    @DeleteMapping("/{veterinarioId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteVeterinario(
            @PathVariable(name = "veterinarioId") final Integer veterinarioId) {
        final ReferencedWarning referencedWarning = veterinarioService.getReferencedWarning(veterinarioId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        veterinarioService.delete(veterinarioId);
        return ResponseEntity.noContent().build();
    }

}
