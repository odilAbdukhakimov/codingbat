package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.spring_boot_security_web.entity.TaskEntity;
import uz.pdp.spring_boot_security_web.entity.TestCaseEntity;
import uz.pdp.spring_boot_security_web.entity.TopicEntity;
import uz.pdp.spring_boot_security_web.model.dto.TaskRequestDTO;
import uz.pdp.spring_boot_security_web.model.dto.TestCaseDto;
import uz.pdp.spring_boot_security_web.model.dto.TopicRequestDTO;
import uz.pdp.spring_boot_security_web.repository.TaskRepository;
import uz.pdp.spring_boot_security_web.service.TaskService;
import uz.pdp.spring_boot_security_web.service.TestCaseService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class TestCaseController {

    private final TestCaseService testCaseService;
    private final TaskService taskService;

    @GetMapping("admin/{language}/{topic}/{task}/{id}")
    public ModelAndView testCaseHome(@PathVariable int id,
                                   ModelAndView model) {

        TaskEntity byId = taskService.getById(id);
        model.addObject("task", byId);
        model.addObject("testCaseList", testCaseService.testCasesOfQuestion(id));
        model.setViewName("TestCase");
        return model;
    }
    @PostMapping("/admin/testCase/add")
    public String addTestCase(
            @ModelAttribute TestCaseDto testCaseDto
    ) {
        testCaseService.add(testCaseDto);
        return "redirect:/admin/lang";
    }

    @GetMapping("/admin/testCase/del/{id}")
    public String deleteTestCase(
            @PathVariable int id
    ) {
        testCaseService.delete(id);
        return "redirect:/admin/lang";
    }

    @PostMapping("/admin/testCase/update/{id}")
    public String updateTask(
            @PathVariable int id,
            @ModelAttribute TestCaseDto testCaseDto
    ) {
        testCaseService.update(id, testCaseDto);
        return "redirect:/admin/lang";
    }




}
