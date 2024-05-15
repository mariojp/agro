package io.ucsal.agro.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.ucsal.agro.model.InseminacaoDTO;
import io.ucsal.agro.service.InseminacaoService;
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
@RequestMapping(value = "/api/inseminacaos", produces = MediaType.APPLICATION_JSON_VALUE)
public class InseminacaoResource {

    private final InseminacaoService inseminacaoService;

    public InseminacaoResource(final InseminacaoService inseminacaoService) {
        this.inseminacaoService = inseminacaoService;
    }

    @GetMapping
    public ResponseEntity<List<InseminacaoDTO>> getAllInseminacaos() {
        return ResponseEntity.ok(inseminacaoService.findAll());
    }

    @GetMapping("/{inseminacaoId}")
    public ResponseEntity<InseminacaoDTO> getInseminacao(
            @PathVariable(name = "inseminacaoId") final Integer inseminacaoId) {
        return ResponseEntity.ok(inseminacaoService.get(inseminacaoId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createInseminacao(
            @RequestBody @Valid final InseminacaoDTO inseminacaoDTO) {
        final Integer createdInseminacaoId = inseminacaoService.create(inseminacaoDTO);
        return new ResponseEntity<>(createdInseminacaoId, HttpStatus.CREATED);
    }

    @PutMapping("/{inseminacaoId}")
    public ResponseEntity<Integer> updateInseminacao(
            @PathVariable(name = "inseminacaoId") final Integer inseminacaoId,
            @RequestBody @Valid final InseminacaoDTO inseminacaoDTO) {
        inseminacaoService.update(inseminacaoId, inseminacaoDTO);
        return ResponseEntity.ok(inseminacaoId);
    }

    @DeleteMapping("/{inseminacaoId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteInseminacao(
            @PathVariable(name = "inseminacaoId") final Integer inseminacaoId) {
        final ReferencedWarning referencedWarning = inseminacaoService.getReferencedWarning(inseminacaoId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        inseminacaoService.delete(inseminacaoId);
        return ResponseEntity.noContent().build();
    }

}
