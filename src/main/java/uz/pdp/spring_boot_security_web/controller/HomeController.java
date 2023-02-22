package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.spring_boot_security_web.service.LanguageService;
import uz.pdp.spring_boot_security_web.service.TopicService;

@Controller
@RequiredArgsConstructor
@RequestMapping()
public class HomeController {

    private final LanguageService languageService;
    private final TopicService topicService;

    @ResponseBody
    @GetMapping("/")
    public ModelAndView home(
           ModelAndView modelAndView
            ){
       modelAndView.addObject("subjectList", languageService.languageEntityList());
       modelAndView.addObject("lang","Java");
       modelAndView.addObject("topicList", languageService.getSubject("Java").getTopicEntities());
       modelAndView.setViewName("index");
       return modelAndView;
    }

    @GetMapping("/{language}")
    public ModelAndView getTopicList(
            ModelAndView model,
            @PathVariable String language
    ) {
        model.addObject("subjectList", languageService.languageEntityList());
        model.addObject("lang", language);
        model.addObject("topicList", topicService.getList(language));
        model.setViewName("index");
        return model;
    }
}