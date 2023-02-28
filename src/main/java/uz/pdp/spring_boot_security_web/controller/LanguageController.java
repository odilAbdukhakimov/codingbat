package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.spring_boot_security_web.model.dto.LanguageRequestDTO;
import uz.pdp.spring_boot_security_web.service.LanguageService;
import uz.pdp.spring_boot_security_web.service.TaskService;
import uz.pdp.spring_boot_security_web.service.TopicService;

@Controller
@RequestMapping("/admin/lang")
@RequiredArgsConstructor
public class LanguageController {
    private final LanguageService languageService;

    @GetMapping("")
    public ModelAndView langPage(ModelAndView model) {
        model.addObject("subjectList", languageService.languageEntityList());
        model.setViewName("language");
        return model;
    }

    @ResponseBody
    @PostMapping("/add")
    public String addLanguage(
            @ModelAttribute LanguageRequestDTO languageRequestDTO
    ) {
        languageService.addLanguage(languageRequestDTO);
        return "redirect:/admin/lang";
    }

    @GetMapping("/del/{id}")
    public String deleteLanguage(
            @PathVariable int id
    ) {
        languageService.delete(id);
        return "redirect:/admin/lang";
    }


    @PostMapping("/update/{id}")
    public String updateLanguage(
            @PathVariable("id") int id,
            @ModelAttribute LanguageRequestDTO title
    ) {
        languageService.update(id, title);
        return "redirect:/admin/lang";
    }
}
