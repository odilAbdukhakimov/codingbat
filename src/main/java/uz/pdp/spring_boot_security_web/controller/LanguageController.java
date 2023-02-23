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
    public  String langPage(ModelAndView model){
        model.addObject("subjectList", languageService.languageEntityList());
        return "language";
    }

    @PostMapping("/add")
    public String addLanguage(
            @ModelAttribute LanguageRequestDTO languageRequestDTO
    ) {
        languageService.addLanguage(languageRequestDTO);
        return "language";
    }

    @DeleteMapping("/del/{id}")
    public String deleteLanguage(
            @PathVariable int id
    ) {
        languageService.delete(id);
        return "redirect:/";
    }

    @PutMapping("/update/{title}")
    public String updateLanguage(
            @PathVariable String title,
            @ModelAttribute LanguageRequestDTO languageRequestDTO
    ) {
        languageService.update(title, languageRequestDTO);
        return "redirect:/";
    }
}
