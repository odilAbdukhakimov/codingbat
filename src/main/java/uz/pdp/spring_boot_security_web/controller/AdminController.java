package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uz.pdp.spring_boot_security_web.model.dto.receive.UserRegisterDTO;
import uz.pdp.spring_boot_security_web.service.UserService;

@Controller
@RequestMapping("/api/admin/")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @PutMapping("/update/{name}")
    public String updateAdmin(@PathVariable("name") String name ,@ModelAttribute UserRegisterDTO userRegisterDTO){

        userService.update(name, userRegisterDTO);
        return "CrudAdmin";
    }
    @GetMapping("/show")
    public String showAdmin(){
        return "CrudAdmin";
    }
    @PostMapping("/add")
    public String addUser(
            @ModelAttribute UserRegisterDTO userRegisterDTO
    ) {

        userService.addAdmin(userRegisterDTO);
        return "CrudAdmin";
    }
}
