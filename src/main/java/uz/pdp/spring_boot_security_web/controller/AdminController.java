package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.model.dto.AdminRequestDto;
import uz.pdp.spring_boot_security_web.model.dto.receive.UserRegisterDTO;
import uz.pdp.spring_boot_security_web.repository.UserRepository;
import uz.pdp.spring_boot_security_web.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/add")
    public String addUser(
            @ModelAttribute AdminRequestDto adminRequestDto
    ) {
        userService.addAdmin(adminRequestDto);
        return "redirect:/api/admin";
    }

    @GetMapping("")
    public ModelAndView getAdmins(
            ModelAndView modelAndView
    ) {

        modelAndView.setViewName("CrudAdmin");
        modelAndView.addObject("adminList", userService.adminEntityList());
        return modelAndView;

    }
    @ResponseBody

    @PostMapping("/update/{id}")
    public String update(
            @PathVariable("id") int id,
            @ModelAttribute AdminRequestDto updateAdmin
    ) {
        userService.updateAdmin(id, updateAdmin);
        return "redirect:/api/admin";
    }

    @ResponseBody
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        userService.delete(id);
        return "redirect:/api/admin";
    }
}
