package uz.pdp.spring_boot_security_web.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.model.dto.receive.UserRegisterDTO;
import uz.pdp.spring_boot_security_web.service.UserService;

@Controller
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/add")
    public String addUser(
            @ModelAttribute UserRegisterDTO userRegisterDTO
    ) {

        boolean isSuccess = userService.addUser(userRegisterDTO);
        if (isSuccess) {
            return "redirect:/login";
        } else {
            return "redirect:/register";
        }
    }

    @GetMapping("/update")
    public String updateUser( Model model) {
        UserEntity currentUser = userService.getCurrentUser();
        if (currentUser!=null){
            model.addAttribute("currentUser",currentUser);
        }
        return "updateUser";
    }

    @PostMapping("/update/{username}")
    public String updateUser(@PathVariable("username") String username,
                             UserRegisterDTO userRegisterDTO,
                             Model model,
                             HttpServletRequest request) {
        UserEntity userEntity = userService.updateUser(username, userRegisterDTO);
        UserEntity currentUser = userService.getCurrentUser();
        if (currentUser!=null){
            userService.updateUserContextHolder(userEntity,request);
            UserEntity serviceCurrentUser = userService.getCurrentUser();
            if (serviceCurrentUser!=null){
                model.addAttribute("currentUser",serviceCurrentUser);
            }
            return "index";
        }else {
            return "register";
        }
    }
}
