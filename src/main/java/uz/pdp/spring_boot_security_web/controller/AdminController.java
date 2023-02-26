package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.model.dto.receive.UserRegisterDTO;
import uz.pdp.spring_boot_security_web.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @PostMapping("/update/{username}")
    public String updateAdmin(@PathVariable("username") String username ,@ModelAttribute UserRegisterDTO userRegisterDTO , Model model){

        userService.update(username, userRegisterDTO);
        model.addAttribute("adminList", userService.getALlAdmins());
        return "CrudAdmin";
    }
    @GetMapping("/update/{username}")
    public String update(@PathVariable("username") String username , Model model){
        UserEntity entity=new UserEntity();
        entity.setUsername(username);
        model.addAttribute("userEntity",entity);
        return "updateAdmin";
    }
    @GetMapping("/show")
    public String showAdmin(Model model){
        model.addAttribute("adminList",userService.getALlAdmins());
        return "CrudAdmin";
    }

    @GetMapping("/add")
    public  String add(){
        return "addAdmin";
    }
    @PostMapping("/add")
    public String addUser(
            @ModelAttribute UserRegisterDTO userRegisterDTO ,Model model
    ) {
       userService.addAdmin(userRegisterDTO);
       model.addAttribute("adminList",userService.getALlAdmins());
        return "CrudAdmin";
    }

    @GetMapping("")
    public ModelAndView getAdmins(
            ModelAndView modelAndView
    ) {
//        List<UserEntity> userList = userRepository.findAll();
//        List<UserEntity> adminList = new ArrayList<>();
//        for (UserEntity userEntity : userList) {
//            if (userEntity.getRolePermissionEntities().getRoleEnum().contains("ADMIN")) {
//
//                adminList.add(userEntity);
//            }
//        }
        modelAndView.setViewName("CrudAdmin");
        modelAndView.addObject("adminList", userService.adminList());
        return modelAndView;
    }

    @DeleteMapping("/delete/{username}")
    public String delete(@PathVariable("username") String username, Model model){
        userService.delete(username);
        model.addAttribute("adminList",userService.getALlAdmins());
        return "CrudAdmin";
    }
}
