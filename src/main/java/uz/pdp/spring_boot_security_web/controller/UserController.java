package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.spring_boot_security_web.model.dto.receive.UserRegisterDTO;
import uz.pdp.spring_boot_security_web.service.EmailService;
import uz.pdp.spring_boot_security_web.service.UserService;

@Controller
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final EmailService emailService;
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
//    @GetMapping("/verifyEmail/{email}/{emailCode}")
//    public String verifyEmail(@PathVariable String email,@PathVariable String emailCode){
//        boolean isSuccess = userService.verifyEmail(email, emailCode);
//        if (isSuccess){
//            return "";
//        }
//        return "";
//    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email) {
        String message = "<a href='http://localhost:8080/api/user/password/reset/email=" + email + "'>hello</a>";
        emailService.sendMessage(email,message);
        return "index";
    }

    @GetMapping("/password/reset/{email}")
    public ModelAndView passwordReset(@PathVariable String email, ModelAndView modelAndView) {

        modelAndView.addObject("currentUser", userService.findEmail(email));
        modelAndView.setViewName("updateUser2");
        return modelAndView;
    }

    @GetMapping("/update")
    public String updateUser(Model model) {
        UserEntity currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);
        }
        return "updateUser2";
    }

    @PostMapping("/update/{username}")
    public String updateUser(@PathVariable("username") String username,
                             UserRegisterDTO userRegisterDTO,
                             Model model) {
        UserEntity userEntity = userService.updateUser(username, userRegisterDTO);
        UserEntity currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            userService.updateUserContextHolder(userEntity);
            UserEntity serviceCurrentUser = userService.getCurrentUser();
            if (serviceCurrentUser != null) {
                model.addAttribute("currentUser", serviceCurrentUser);
            }
            return "redirect:/";
        } else {
            return "register";
        }
    }


}
