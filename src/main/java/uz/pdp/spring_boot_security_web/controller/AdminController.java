package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.spring_boot_security_web.model.dto.receive.UserRegisterDTO;
import uz.pdp.spring_boot_security_web.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @PostMapping("/add")
    public String addUser(
            @ModelAttribute UserRegisterDTO userRegisterDTO
    ) {

       userService.addAdmin(userRegisterDTO);
        return "CrudAdmin";
    }

    @GetMapping("")
    public ModelAndView getAdmins(
            ModelAndView modelAndView
    ){
//        List<UserEntity> userList = userRepository.findAll();
//        List<UserEntity> adminList = new ArrayList<>();
//        for (UserEntity userEntity : userList) {
//            if (userEntity.getRolePermissionEntities().getRoleEnum().contains("ADMIN")) {
//
//                adminList.add(userEntity);
//            }
//        }
        modelAndView.setViewName("CrudAdmin");
        modelAndView.addObject("adminList",userService.adminList());
        return modelAndView;

    }

//    @ResponseBody
//    @GetMapping("/list")
//    public String adminList(){
//       userService.adminList();
//        return "CrudAdmin";
//    }
}
