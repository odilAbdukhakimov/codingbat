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
    @ResponseBody
    @PostMapping("/add")
    public String addLanguage(
            @ModelAttribute LanguageRequestDTO languageRequestDTO
    ){
        languageService.addLanguage(languageRequestDTO);
        return "redirect:/";
    }
    @ResponseBody
    @DeleteMapping("/del/{id}")
    public String deleteLanguage(
            @PathVariable int id
    ){
        languageService.delete(id);
    return "redirect:/";
    }
    @PutMapping("/update/{title}")
    public String updateLanguage(
            @PathVariable String title,
            @ModelAttribute LanguageRequestDTO languageRequestDTO
    ){
        languageService.update(title, languageRequestDTO);
        return "redirect:/";
    }
}
