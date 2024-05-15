package io.ucsal.agro.service;

import io.ucsal.agro.domain.Animal;
import io.ucsal.agro.domain.Inseminacao;
import io.ucsal.agro.domain.Parto;
import io.ucsal.agro.model.AnimalDTO;
import io.ucsal.agro.repos.AnimalRepository;
import io.ucsal.agro.repos.InseminacaoRepository;
import io.ucsal.agro.repos.PartoRepository;
import io.ucsal.agro.util.NotFoundException;
import io.ucsal.agro.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final PartoRepository partoRepository;
    private final InseminacaoRepository inseminacaoRepository;

    public AnimalService(final AnimalRepository animalRepository,
            final PartoRepository partoRepository,
            final InseminacaoRepository inseminacaoRepository) {
        this.animalRepository = animalRepository;
        this.partoRepository = partoRepository;
        this.inseminacaoRepository = inseminacaoRepository;
    }

    public List<AnimalDTO> findAll() {
        final List<Animal> animals = animalRepository.findAll(Sort.by("animalId"));
        return animals.stream()
                .map(animal -> mapToDTO(animal, new AnimalDTO()))
                .toList();
    }

    public AnimalDTO get(final Integer animalId) {
        return animalRepository.findById(animalId)
                .map(animal -> mapToDTO(animal, new AnimalDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final AnimalDTO animalDTO) {
        final Animal animal = new Animal();
        mapToEntity(animalDTO, animal);
        return animalRepository.save(animal).getAnimalId();
    }

    public void update(final Integer animalId, final AnimalDTO animalDTO) {
        final Animal animal = animalRepository.findById(animalId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(animalDTO, animal);
        animalRepository.save(animal);
    }

    public void delete(final Integer animalId) {
        animalRepository.deleteById(animalId);
    }

    private AnimalDTO mapToDTO(final Animal animal, final AnimalDTO animalDTO) {
        animalDTO.setAnimalId(animal.getAnimalId());
        animalDTO.setNome(animal.getNome());
        animalDTO.setTipo(animal.getTipo());
        animalDTO.setRaca(animal.getRaca());
        animalDTO.setDataNascimento(animal.getDataNascimento());
        animalDTO.setOrigem(animal.getOrigem());
        animalDTO.setSituacao(animal.getSituacao());
        animalDTO.setParto(animal.getParto() == null ? null : animal.getParto().getPartoId());
        animalDTO.setMae(animal.getMae() == null ? null : animal.getMae().getAnimalId());
        animalDTO.setPai(animal.getPai() == null ? null : animal.getPai().getAnimalId());
        return animalDTO;
    }

    private Animal mapToEntity(final AnimalDTO animalDTO, final Animal animal) {
        animal.setNome(animalDTO.getNome());
        animal.setTipo(animalDTO.getTipo());
        animal.setRaca(animalDTO.getRaca());
        animal.setDataNascimento(animalDTO.getDataNascimento());
        animal.setOrigem(animalDTO.getOrigem());
        animal.setSituacao(animalDTO.getSituacao());
        final Parto parto = animalDTO.getParto() == null ? null : partoRepository.findById(animalDTO.getParto())
                .orElseThrow(() -> new NotFoundException("parto not found"));
        animal.setParto(parto);
        final Animal mae = animalDTO.getMae() == null ? null : animalRepository.findById(animalDTO.getMae())
                .orElseThrow(() -> new NotFoundException("mae not found"));
        animal.setMae(mae);
        final Animal pai = animalDTO.getPai() == null ? null : animalRepository.findById(animalDTO.getPai())
                .orElseThrow(() -> new NotFoundException("pai not found"));
        animal.setPai(pai);
        return animal;
    }

    public ReferencedWarning getReferencedWarning(final Integer animalId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Animal animal = animalRepository.findById(animalId)
                .orElseThrow(NotFoundException::new);
        final Animal maeAnimal = animalRepository.findFirstByMaeAndAnimalIdNot(animal, animal.getAnimalId());
        if (maeAnimal != null) {
            referencedWarning.setKey("animal.animal.mae.referenced");
            referencedWarning.addParam(maeAnimal.getAnimalId());
            return referencedWarning;
        }
        final Animal paiAnimal = animalRepository.findFirstByPaiAndAnimalIdNot(animal, animal.getAnimalId());
        if (paiAnimal != null) {
            referencedWarning.setKey("animal.animal.pai.referenced");
            referencedWarning.addParam(paiAnimal.getAnimalId());
            return referencedWarning;
        }
        final Inseminacao vacaInseminacao = inseminacaoRepository.findFirstByVaca(animal);
        if (vacaInseminacao != null) {
            referencedWarning.setKey("animal.inseminacao.vaca.referenced");
            referencedWarning.addParam(vacaInseminacao.getInseminacaoId());
            return referencedWarning;
        }
        final Inseminacao touroInseminacao = inseminacaoRepository.findFirstByTouro(animal);
        if (touroInseminacao != null) {
            referencedWarning.setKey("animal.inseminacao.touro.referenced");
            referencedWarning.addParam(touroInseminacao.getInseminacaoId());
            return referencedWarning;
        }
        return null;
    }

}
