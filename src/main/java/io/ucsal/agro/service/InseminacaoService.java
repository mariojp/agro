package io.ucsal.agro.service;

import io.ucsal.agro.domain.Animal;
import io.ucsal.agro.domain.Inseminacao;
import io.ucsal.agro.domain.Parto;
import io.ucsal.agro.domain.Veterinario;
import io.ucsal.agro.model.InseminacaoDTO;
import io.ucsal.agro.repos.AnimalRepository;
import io.ucsal.agro.repos.InseminacaoRepository;
import io.ucsal.agro.repos.PartoRepository;
import io.ucsal.agro.repos.VeterinarioRepository;
import io.ucsal.agro.util.NotFoundException;
import io.ucsal.agro.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class InseminacaoService {

    private final InseminacaoRepository inseminacaoRepository;
    private final AnimalRepository animalRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final PartoRepository partoRepository;

    public InseminacaoService(final InseminacaoRepository inseminacaoRepository,
            final AnimalRepository animalRepository,
            final VeterinarioRepository veterinarioRepository,
            final PartoRepository partoRepository) {
        this.inseminacaoRepository = inseminacaoRepository;
        this.animalRepository = animalRepository;
        this.veterinarioRepository = veterinarioRepository;
        this.partoRepository = partoRepository;
    }

    public List<InseminacaoDTO> findAll() {
        final List<Inseminacao> inseminacaos = inseminacaoRepository.findAll(Sort.by("inseminacaoId"));
        return inseminacaos.stream()
                .map(inseminacao -> mapToDTO(inseminacao, new InseminacaoDTO()))
                .toList();
    }

    public InseminacaoDTO get(final Integer inseminacaoId) {
        return inseminacaoRepository.findById(inseminacaoId)
                .map(inseminacao -> mapToDTO(inseminacao, new InseminacaoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final InseminacaoDTO inseminacaoDTO) {
        final Inseminacao inseminacao = new Inseminacao();
        mapToEntity(inseminacaoDTO, inseminacao);
        return inseminacaoRepository.save(inseminacao).getInseminacaoId();
    }

    public void update(final Integer inseminacaoId, final InseminacaoDTO inseminacaoDTO) {
        final Inseminacao inseminacao = inseminacaoRepository.findById(inseminacaoId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(inseminacaoDTO, inseminacao);
        inseminacaoRepository.save(inseminacao);
    }

    public void delete(final Integer inseminacaoId) {
        inseminacaoRepository.deleteById(inseminacaoId);
    }

    private InseminacaoDTO mapToDTO(final Inseminacao inseminacao,
            final InseminacaoDTO inseminacaoDTO) {
        inseminacaoDTO.setInseminacaoId(inseminacao.getInseminacaoId());
        inseminacaoDTO.setDataCobertura(inseminacao.getDataCobertura());
        inseminacaoDTO.setObservacoes(inseminacao.getObservacoes());
        inseminacaoDTO.setMetodo(inseminacao.getMetodo());
        inseminacaoDTO.setSituacao(inseminacao.getSituacao());
        inseminacaoDTO.setVaca(inseminacao.getVaca() == null ? null : inseminacao.getVaca().getAnimalId());
        inseminacaoDTO.setTouro(inseminacao.getTouro() == null ? null : inseminacao.getTouro().getAnimalId());
        inseminacaoDTO.setVeterinario(inseminacao.getVeterinario() == null ? null : inseminacao.getVeterinario().getVeterinarioId());
        return inseminacaoDTO;
    }

    private Inseminacao mapToEntity(final InseminacaoDTO inseminacaoDTO,
            final Inseminacao inseminacao) {
        inseminacao.setDataCobertura(inseminacaoDTO.getDataCobertura());
        inseminacao.setObservacoes(inseminacaoDTO.getObservacoes());
        inseminacao.setMetodo(inseminacaoDTO.getMetodo());
        inseminacao.setSituacao(inseminacaoDTO.getSituacao());
        final Animal vaca = inseminacaoDTO.getVaca() == null ? null : animalRepository.findById(inseminacaoDTO.getVaca())
                .orElseThrow(() -> new NotFoundException("vaca not found"));
        inseminacao.setVaca(vaca);
        final Animal touro = inseminacaoDTO.getTouro() == null ? null : animalRepository.findById(inseminacaoDTO.getTouro())
                .orElseThrow(() -> new NotFoundException("touro not found"));
        inseminacao.setTouro(touro);
        final Veterinario veterinario = inseminacaoDTO.getVeterinario() == null ? null : veterinarioRepository.findById(inseminacaoDTO.getVeterinario())
                .orElseThrow(() -> new NotFoundException("veterinario not found"));
        inseminacao.setVeterinario(veterinario);
        return inseminacao;
    }

    public ReferencedWarning getReferencedWarning(final Integer inseminacaoId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Inseminacao inseminacao = inseminacaoRepository.findById(inseminacaoId)
                .orElseThrow(NotFoundException::new);
        final Parto inseminacaoParto = partoRepository.findFirstByInseminacao(inseminacao);
        if (inseminacaoParto != null) {
            referencedWarning.setKey("inseminacao.parto.inseminacao.referenced");
            referencedWarning.addParam(inseminacaoParto.getPartoId());
            return referencedWarning;
        }
        return null;
    }

}
