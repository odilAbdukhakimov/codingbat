package uz.pdp.spring_boot_security_web.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import org.springframework.web.servlet.ModelAndView;
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
            return "redirect:/";
        } else {
            return "redirect:/register";
        }

    }
    @GetMapping("/verifyEmail/{email}/{emailCode}")
    public String verifyEmail(@PathVariable String email,@PathVariable String emailCode){
        boolean isSuccess = userService.verifyEmail(email, emailCode);
        if (isSuccess){
            return "";
        }
        return "";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email){
        userService.resetPassword(email);
        return "index";
    }
@GetMapping("/password/reset/{email}")
    public ModelAndView passwordReset(@PathVariable String email, ModelAndView modelAndView){

       modelAndView.addObject("currentUser", userService.findEmail(email));
       modelAndView.setViewName("updateUser2");
       return modelAndView;
}


}
