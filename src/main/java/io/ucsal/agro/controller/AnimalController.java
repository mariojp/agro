package io.ucsal.agro.controller;

import io.ucsal.agro.domain.Animal;
import io.ucsal.agro.domain.Parto;
import io.ucsal.agro.model.AnimalDTO;
import io.ucsal.agro.model.AnimalRaca;
import io.ucsal.agro.model.AnimalSituacao;
import io.ucsal.agro.model.AnimalTipo;
import io.ucsal.agro.repos.AnimalRepository;
import io.ucsal.agro.repos.PartoRepository;
import io.ucsal.agro.service.AnimalService;
import io.ucsal.agro.util.CustomCollectors;
import io.ucsal.agro.util.ReferencedWarning;
import io.ucsal.agro.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/animals")
public class AnimalController {

    private final AnimalService animalService;
    private final PartoRepository partoRepository;
    private final AnimalRepository animalRepository;

    public AnimalController(final AnimalService animalService,
            final PartoRepository partoRepository, final AnimalRepository animalRepository) {
        this.animalService = animalService;
        this.partoRepository = partoRepository;
        this.animalRepository = animalRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("tipoValues", AnimalTipo.values());
        model.addAttribute("racaValues", AnimalRaca.values());
        model.addAttribute("situacaoValues", AnimalSituacao.values());
        model.addAttribute("partoValues", partoRepository.findAll(Sort.by("partoId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Parto::getPartoId, Parto::getPartoId)));
        model.addAttribute("maeValues", animalRepository.findAll(Sort.by("animalId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Animal::getAnimalId, Animal::getNome)));
        model.addAttribute("paiValues", animalRepository.findAll(Sort.by("animalId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Animal::getAnimalId, Animal::getNome)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("animals", animalService.findAll());
        return "animal/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("animal") final AnimalDTO animalDTO) {
        return "animal/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("animal") @Valid final AnimalDTO animalDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "animal/add";
        }
        animalService.create(animalDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("animal.create.success"));
        return "redirect:/animals";
    }

    @GetMapping("/edit/{animalId}")
    public String edit(@PathVariable(name = "animalId") final Integer animalId, final Model model) {
        model.addAttribute("animal", animalService.get(animalId));
        return "animal/edit";
    }

    @PostMapping("/edit/{animalId}")
    public String edit(@PathVariable(name = "animalId") final Integer animalId,
            @ModelAttribute("animal") @Valid final AnimalDTO animalDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "animal/edit";
        }
        animalService.update(animalId, animalDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("animal.update.success"));
        return "redirect:/animals";
    }

    @PostMapping("/delete/{animalId}")
    public String delete(@PathVariable(name = "animalId") final Integer animalId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = animalService.getReferencedWarning(animalId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            animalService.delete(animalId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("animal.delete.success"));
        }
        return "redirect:/animals";
    }

}
