package io.ucsal.agro.service;

import io.ucsal.agro.domain.Animal;
import io.ucsal.agro.domain.Inseminacao;
import io.ucsal.agro.domain.Parto;
import io.ucsal.agro.model.PartoDTO;
import io.ucsal.agro.repos.AnimalRepository;
import io.ucsal.agro.repos.InseminacaoRepository;
import io.ucsal.agro.repos.PartoRepository;
import io.ucsal.agro.util.NotFoundException;
import io.ucsal.agro.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PartoService {

    private final PartoRepository partoRepository;
    private final InseminacaoRepository inseminacaoRepository;
    private final AnimalRepository animalRepository;

    public PartoService(final PartoRepository partoRepository,
            final InseminacaoRepository inseminacaoRepository,
            final AnimalRepository animalRepository) {
        this.partoRepository = partoRepository;
        this.inseminacaoRepository = inseminacaoRepository;
        this.animalRepository = animalRepository;
    }

    public List<PartoDTO> findAll() {
        final List<Parto> partoes = partoRepository.findAll(Sort.by("partoId"));
        return partoes.stream()
                .map(parto -> mapToDTO(parto, new PartoDTO()))
                .toList();
    }

    public PartoDTO get(final Integer partoId) {
        return partoRepository.findById(partoId)
                .map(parto -> mapToDTO(parto, new PartoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final PartoDTO partoDTO) {
        final Parto parto = new Parto();
        mapToEntity(partoDTO, parto);
        return partoRepository.save(parto).getPartoId();
    }

    public void update(final Integer partoId, final PartoDTO partoDTO) {
        final Parto parto = partoRepository.findById(partoId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(partoDTO, parto);
        partoRepository.save(parto);
    }

    public void delete(final Integer partoId) {
        partoRepository.deleteById(partoId);
    }

    private PartoDTO mapToDTO(final Parto parto, final PartoDTO partoDTO) {
        partoDTO.setPartoId(parto.getPartoId());
        partoDTO.setDataParto(parto.getDataParto());
        partoDTO.setObservacoes(parto.getObservacoes());
        partoDTO.setInseminacao(parto.getInseminacao() == null ? null : parto.getInseminacao().getInseminacaoId());
        return partoDTO;
    }

    private Parto mapToEntity(final PartoDTO partoDTO, final Parto parto) {
        parto.setDataParto(partoDTO.getDataParto());
        parto.setObservacoes(partoDTO.getObservacoes());
        final Inseminacao inseminacao = partoDTO.getInseminacao() == null ? null : inseminacaoRepository.findById(partoDTO.getInseminacao())
                .orElseThrow(() -> new NotFoundException("inseminacao not found"));
        parto.setInseminacao(inseminacao);
        return parto;
    }

    public ReferencedWarning getReferencedWarning(final Integer partoId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Parto parto = partoRepository.findById(partoId)
                .orElseThrow(NotFoundException::new);
        final Animal partoAnimal = animalRepository.findFirstByParto(parto);
        if (partoAnimal != null) {
            referencedWarning.setKey("parto.animal.parto.referenced");
            referencedWarning.addParam(partoAnimal.getAnimalId());
            return referencedWarning;
        }
        return null;
    }

}
