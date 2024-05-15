package io.ucsal.agro.service;

import io.ucsal.agro.domain.Inseminacao;
import io.ucsal.agro.domain.Veterinario;
import io.ucsal.agro.model.VeterinarioDTO;
import io.ucsal.agro.repos.InseminacaoRepository;
import io.ucsal.agro.repos.VeterinarioRepository;
import io.ucsal.agro.util.NotFoundException;
import io.ucsal.agro.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;
    private final InseminacaoRepository inseminacaoRepository;

    public VeterinarioService(final VeterinarioRepository veterinarioRepository,
            final InseminacaoRepository inseminacaoRepository) {
        this.veterinarioRepository = veterinarioRepository;
        this.inseminacaoRepository = inseminacaoRepository;
    }

    public List<VeterinarioDTO> findAll() {
        final List<Veterinario> veterinarios = veterinarioRepository.findAll(Sort.by("veterinarioId"));
        return veterinarios.stream()
                .map(veterinario -> mapToDTO(veterinario, new VeterinarioDTO()))
                .toList();
    }

    public VeterinarioDTO get(final Integer veterinarioId) {
        return veterinarioRepository.findById(veterinarioId)
                .map(veterinario -> mapToDTO(veterinario, new VeterinarioDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final VeterinarioDTO veterinarioDTO) {
        final Veterinario veterinario = new Veterinario();
        mapToEntity(veterinarioDTO, veterinario);
        return veterinarioRepository.save(veterinario).getVeterinarioId();
    }

    public void update(final Integer veterinarioId, final VeterinarioDTO veterinarioDTO) {
        final Veterinario veterinario = veterinarioRepository.findById(veterinarioId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(veterinarioDTO, veterinario);
        veterinarioRepository.save(veterinario);
    }

    public void delete(final Integer veterinarioId) {
        veterinarioRepository.deleteById(veterinarioId);
    }

    private VeterinarioDTO mapToDTO(final Veterinario veterinario,
            final VeterinarioDTO veterinarioDTO) {
        veterinarioDTO.setVeterinarioId(veterinario.getVeterinarioId());
        veterinarioDTO.setNome(veterinario.getNome());
        veterinarioDTO.setQualificacoes(veterinario.getQualificacoes());
        veterinarioDTO.setContato(veterinario.getContato());
        return veterinarioDTO;
    }

    private Veterinario mapToEntity(final VeterinarioDTO veterinarioDTO,
            final Veterinario veterinario) {
        veterinario.setNome(veterinarioDTO.getNome());
        veterinario.setQualificacoes(veterinarioDTO.getQualificacoes());
        veterinario.setContato(veterinarioDTO.getContato());
        return veterinario;
    }

    public ReferencedWarning getReferencedWarning(final Integer veterinarioId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Veterinario veterinario = veterinarioRepository.findById(veterinarioId)
                .orElseThrow(NotFoundException::new);
        final Inseminacao veterinarioInseminacao = inseminacaoRepository.findFirstByVeterinario(veterinario);
        if (veterinarioInseminacao != null) {
            referencedWarning.setKey("veterinario.inseminacao.veterinario.referenced");
            referencedWarning.addParam(veterinarioInseminacao.getInseminacaoId());
            return referencedWarning;
        }
        return null;
    }

}
