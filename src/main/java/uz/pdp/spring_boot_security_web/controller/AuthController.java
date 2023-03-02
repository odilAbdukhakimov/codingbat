package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.pdp.spring_boot_security_web.model.dto.receive.UserRegisterDTO;
import uz.pdp.spring_boot_security_web.service.UserService;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    @GetMapping("/register")
    public String register(){
        return "createAccount";
    }
    @GetMapping("/forget-password")
    public String forgetPassword(){
        return "forgetPassword";
    }

//    @GetMapping("/")
//    public String currentUser(OAuth2AuthenticationToken authenticationToken){
//        System.out.println("qale");
//        UserRegisterDTO userRegisterDTO=new UserRegisterDTO();
//        userRegisterDTO.setEmail((String) authenticationToken.getPrincipal().getAttributes().get("email"));
//        userRegisterDTO.setName((String) authenticationToken.getPrincipal().getAttributes().get("name"));
//        userRegisterDTO.setUsername((String) authenticationToken.getPrincipal().getAttributes().get("given_name"));
//        System.out.println(authenticationToken.getPrincipal().getAttributes().get("picture"));
//        userService.addUser(userRegisterDTO);
//        return "redirect:/1";
//    }
}
