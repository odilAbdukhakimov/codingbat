package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.spring_boot_security_web.entity.LanguageEntity;
import uz.pdp.spring_boot_security_web.model.dto.LanguageRequestDTO;
import uz.pdp.spring_boot_security_web.model.dto.TopicRequestDTO;
import uz.pdp.spring_boot_security_web.service.LanguageService;
import uz.pdp.spring_boot_security_web.service.TaskService;
import uz.pdp.spring_boot_security_web.service.TopicService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class TopicController {
    private final LanguageService languageService;
    private final TaskService taskService;
    private final TopicService topicService;

    @GetMapping("/{language}/{id}")
    public ModelAndView getTaskList(
            ModelAndView modelAndView,
            @PathVariable String language,
            @PathVariable int id
    ) {
        modelAndView.addObject("subjectList", languageService.languageEntityList());
        modelAndView.addObject("taskList", taskService.getTaskList(id));
        modelAndView.setViewName("task");
        return modelAndView;
    }

    @GetMapping("admin/topic/{id}")
        public ModelAndView topicHome( @PathVariable int id, ModelAndView model){
        LanguageEntity byId = languageService.getById(id);
        model.addObject("language", byId);
        model.addObject("topicList", byId.getTopicEntities());
        model.setViewName("topic");
        return model;
    }

    @PostMapping("/admin/topic/add")
    public String addTopic(
            @ModelAttribute TopicRequestDTO topicRequestDTO
    ) {
        topicService.add(topicRequestDTO);
        return "redirect:/admin/lang";
    }

    @GetMapping("/admin/topic/del/{id}")
    public String deleteTopic(
            @PathVariable int id
    ) {
        topicService.delete(id);
        return "redirect:/admin/lang";
    }

    @PostMapping("/admin/topic/update/{id}")
    public String updateTopic(
            @PathVariable int id,
            @ModelAttribute TopicRequestDTO topicRequestDTO
    ) {
        topicService.update(id, topicRequestDTO);
        return "redirect: ";
    }
}
