package io.ucsal.agro.controller;

import io.ucsal.agro.domain.Inseminacao;
import io.ucsal.agro.model.PartoDTO;
import io.ucsal.agro.repos.InseminacaoRepository;
import io.ucsal.agro.service.PartoService;
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
@RequestMapping("/partos")
public class PartoController {

    private final PartoService partoService;
    private final InseminacaoRepository inseminacaoRepository;

    public PartoController(final PartoService partoService,
            final InseminacaoRepository inseminacaoRepository) {
        this.partoService = partoService;
        this.inseminacaoRepository = inseminacaoRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("inseminacaoValues", inseminacaoRepository.findAll(Sort.by("inseminacaoId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Inseminacao::getInseminacaoId, Inseminacao::getInseminacaoId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("partoes", partoService.findAll());
        return "parto/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("parto") final PartoDTO partoDTO) {
        return "parto/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("parto") @Valid final PartoDTO partoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "parto/add";
        }
        partoService.create(partoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("parto.create.success"));
        return "redirect:/partos";
    }

    @GetMapping("/edit/{partoId}")
    public String edit(@PathVariable(name = "partoId") final Integer partoId, final Model model) {
        model.addAttribute("parto", partoService.get(partoId));
        return "parto/edit";
    }

    @PostMapping("/edit/{partoId}")
    public String edit(@PathVariable(name = "partoId") final Integer partoId,
            @ModelAttribute("parto") @Valid final PartoDTO partoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "parto/edit";
        }
        partoService.update(partoId, partoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("parto.update.success"));
        return "redirect:/partos";
    }

    @PostMapping("/delete/{partoId}")
    public String delete(@PathVariable(name = "partoId") final Integer partoId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = partoService.getReferencedWarning(partoId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            partoService.delete(partoId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("parto.delete.success"));
        }
        return "redirect:/partos";
    }

}
