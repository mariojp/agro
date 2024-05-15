package io.ucsal.agro.controller;

import io.ucsal.agro.domain.Animal;
import io.ucsal.agro.domain.Veterinario;
import io.ucsal.agro.model.InseminacaoDTO;
import io.ucsal.agro.model.InseminacaoMetodo;
import io.ucsal.agro.model.InseminacaoSituacao;
import io.ucsal.agro.repos.AnimalRepository;
import io.ucsal.agro.repos.VeterinarioRepository;
import io.ucsal.agro.service.InseminacaoService;
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
@RequestMapping("/inseminacaos")
public class InseminacaoController {

    private final InseminacaoService inseminacaoService;
    private final AnimalRepository animalRepository;
    private final VeterinarioRepository veterinarioRepository;

    public InseminacaoController(final InseminacaoService inseminacaoService,
            final AnimalRepository animalRepository,
            final VeterinarioRepository veterinarioRepository) {
        this.inseminacaoService = inseminacaoService;
        this.animalRepository = animalRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("metodoValues", InseminacaoMetodo.values());
        model.addAttribute("situacaoValues", InseminacaoSituacao.values());
        model.addAttribute("vacaValues", animalRepository.findAll(Sort.by("animalId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Animal::getAnimalId, Animal::getNome)));
        model.addAttribute("touroValues", animalRepository.findAll(Sort.by("animalId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Animal::getAnimalId, Animal::getNome)));
        model.addAttribute("veterinarioValues", veterinarioRepository.findAll(Sort.by("veterinarioId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Veterinario::getVeterinarioId, Veterinario::getNome)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("inseminacaos", inseminacaoService.findAll());
        return "inseminacao/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("inseminacao") final InseminacaoDTO inseminacaoDTO) {
        return "inseminacao/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("inseminacao") @Valid final InseminacaoDTO inseminacaoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "inseminacao/add";
        }
        inseminacaoService.create(inseminacaoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("inseminacao.create.success"));
        return "redirect:/inseminacaos";
    }

    @GetMapping("/edit/{inseminacaoId}")
    public String edit(@PathVariable(name = "inseminacaoId") final Integer inseminacaoId,
            final Model model) {
        model.addAttribute("inseminacao", inseminacaoService.get(inseminacaoId));
        return "inseminacao/edit";
    }

    @PostMapping("/edit/{inseminacaoId}")
    public String edit(@PathVariable(name = "inseminacaoId") final Integer inseminacaoId,
            @ModelAttribute("inseminacao") @Valid final InseminacaoDTO inseminacaoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "inseminacao/edit";
        }
        inseminacaoService.update(inseminacaoId, inseminacaoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("inseminacao.update.success"));
        return "redirect:/inseminacaos";
    }

    @PostMapping("/delete/{inseminacaoId}")
    public String delete(@PathVariable(name = "inseminacaoId") final Integer inseminacaoId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = inseminacaoService.getReferencedWarning(inseminacaoId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            inseminacaoService.delete(inseminacaoId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("inseminacao.delete.success"));
        }
        return "redirect:/inseminacaos";
    }

}
