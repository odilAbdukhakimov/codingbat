package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.spring_boot_security_web.entity.LanguageEntity;
import uz.pdp.spring_boot_security_web.entity.TopicEntity;
import uz.pdp.spring_boot_security_web.model.dto.TaskRequestDTO;
import uz.pdp.spring_boot_security_web.model.dto.TopicRequestDTO;
import uz.pdp.spring_boot_security_web.service.LanguageService;
import uz.pdp.spring_boot_security_web.service.TaskService;
import uz.pdp.spring_boot_security_web.service.TopicService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class TaskController {
    private final LanguageService languageService;
    private final TaskService taskService;
    private final TopicService topicService;

    @GetMapping("/task/{id}")
    public ModelAndView compPage(
            ModelAndView modelAndView,
            @PathVariable int id
    ) {
        modelAndView.addObject("task", taskService.getById(id));
        modelAndView.setViewName("compiler");
        return modelAndView;
    }

    @GetMapping("admin/{language}/{topic}/{id}")
    public ModelAndView topicHome(@PathVariable int id, ModelAndView model) {
        TopicEntity byId = topicService.getById(id);
        model.addObject("topic", byId);
        model.addObject("taskList", byId.getTaskEntityList());
        model.setViewName("task");
        return model;
    }

    @PostMapping("/admin/task/add")
    public String addTask(
            @ModelAttribute TaskRequestDTO taskRequestDTO
    ) {
        taskService.addTask(taskRequestDTO);
        return "redirect:/admin/lang";
    }

    @GetMapping("/admin/task/del/{id}")
    public String deleteTask(
            @PathVariable int id
    ) {
        taskService.delete(id);
        return "redirect:/admin/lang";
    }

    @PostMapping("/admin/task/update/{id}")
    public String updateTask(
            @PathVariable int id,
            @ModelAttribute TaskRequestDTO taskRequestDTO
    ) {
        taskService.update(id,taskRequestDTO);
        return "redirect:/admin/lang";
    }
}
