package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.spring_boot_security_web.entity.TaskEntity;
import uz.pdp.spring_boot_security_web.entity.TestCaseEntity;
import uz.pdp.spring_boot_security_web.model.dto.TestCaseDto;
import uz.pdp.spring_boot_security_web.service.TaskService;
import uz.pdp.spring_boot_security_web.service.TestCaseService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/case")
public class TestCaseController {
private final TestCaseService testCaseService;
    @GetMapping("/Task-id/{id}")
    public ModelAndView TestCaseList(
            @PathVariable int id,
            ModelAndView modelAndView
    ) {
        List<TestCaseEntity> testCaseEntities = testCaseService.testCasesOfQuestion(id);
        modelAndView.addObject("TestCaseList", testCaseEntities);
        modelAndView.setViewName("testCase");
        return modelAndView;
    }

    @PostMapping("/add")
    public String addTestCase(
            @ModelAttribute TestCaseDto testCaseDto,
            Model model
            ){
        testCaseService.add(testCaseDto);
        return "redirect:/admin/lang";
    }

}
