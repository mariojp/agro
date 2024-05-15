package io.ucsal.agro.controller;

import io.ucsal.agro.model.VeterinarioDTO;
import io.ucsal.agro.service.VeterinarioService;
import io.ucsal.agro.util.ReferencedWarning;
import io.ucsal.agro.util.WebUtils;
import jakarta.validation.Valid;
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
@RequestMapping("/veterinarios")
public class VeterinarioController {

    private final VeterinarioService veterinarioService;

    public VeterinarioController(final VeterinarioService veterinarioService) {
        this.veterinarioService = veterinarioService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("veterinarios", veterinarioService.findAll());
        return "veterinario/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("veterinario") final VeterinarioDTO veterinarioDTO) {
        return "veterinario/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("veterinario") @Valid final VeterinarioDTO veterinarioDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "veterinario/add";
        }
        veterinarioService.create(veterinarioDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("veterinario.create.success"));
        return "redirect:/veterinarios";
    }

    @GetMapping("/edit/{veterinarioId}")
    public String edit(@PathVariable(name = "veterinarioId") final Integer veterinarioId,
            final Model model) {
        model.addAttribute("veterinario", veterinarioService.get(veterinarioId));
        return "veterinario/edit";
    }

    @PostMapping("/edit/{veterinarioId}")
    public String edit(@PathVariable(name = "veterinarioId") final Integer veterinarioId,
            @ModelAttribute("veterinario") @Valid final VeterinarioDTO veterinarioDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "veterinario/edit";
        }
        veterinarioService.update(veterinarioId, veterinarioDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("veterinario.update.success"));
        return "redirect:/veterinarios";
    }

    @PostMapping("/delete/{veterinarioId}")
    public String delete(@PathVariable(name = "veterinarioId") final Integer veterinarioId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = veterinarioService.getReferencedWarning(veterinarioId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            veterinarioService.delete(veterinarioId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("veterinario.delete.success"));
        }
        return "redirect:/veterinarios";
    }

}
