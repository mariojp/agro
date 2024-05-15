package io.ucsal.agro.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.ucsal.agro.model.PartoDTO;
import io.ucsal.agro.service.PartoService;
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
@RequestMapping(value = "/api/partos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PartoResource {

    private final PartoService partoService;

    public PartoResource(final PartoService partoService) {
        this.partoService = partoService;
    }

    @GetMapping
    public ResponseEntity<List<PartoDTO>> getAllPartos() {
        return ResponseEntity.ok(partoService.findAll());
    }

    @GetMapping("/{partoId}")
    public ResponseEntity<PartoDTO> getParto(
            @PathVariable(name = "partoId") final Integer partoId) {
        return ResponseEntity.ok(partoService.get(partoId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createParto(@RequestBody @Valid final PartoDTO partoDTO) {
        final Integer createdPartoId = partoService.create(partoDTO);
        return new ResponseEntity<>(createdPartoId, HttpStatus.CREATED);
    }

    @PutMapping("/{partoId}")
    public ResponseEntity<Integer> updateParto(
            @PathVariable(name = "partoId") final Integer partoId,
            @RequestBody @Valid final PartoDTO partoDTO) {
        partoService.update(partoId, partoDTO);
        return ResponseEntity.ok(partoId);
    }

    @DeleteMapping("/{partoId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteParto(@PathVariable(name = "partoId") final Integer partoId) {
        final ReferencedWarning referencedWarning = partoService.getReferencedWarning(partoId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        partoService.delete(partoId);
        return ResponseEntity.noContent().build();
    }

}
