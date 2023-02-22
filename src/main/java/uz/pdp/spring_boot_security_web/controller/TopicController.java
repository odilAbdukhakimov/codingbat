package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.spring_boot_security_web.service.LanguageService;
import uz.pdp.spring_boot_security_web.service.TaskService;
import uz.pdp.spring_boot_security_web.service.TopicService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class TopicController {
    private final LanguageService languageService;
    private final TopicService topicService;
//    @GetMapping("/{language}/{id}")
//    public ModelAndView getTaskList(
//            ModelAndView modelAndView,
//            @PathVariable String language,
//            @PathVariable int id
//    ) {
//        modelAndView.addObject("subjectList", languageService.languageEntityList());
//        modelAndView.addObject("taskList", taskService.getTaskList(id));
//        modelAndView.setViewName("task");
//        return modelAndView;
//    }
}