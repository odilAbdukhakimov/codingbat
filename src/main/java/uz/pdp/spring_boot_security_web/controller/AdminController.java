package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
