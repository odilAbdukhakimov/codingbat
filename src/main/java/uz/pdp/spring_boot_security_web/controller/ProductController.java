package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.pdp.spring_boot_security_web.service.AuthService;

@Controller
@RequestMapping("/api/product/{product}")
public class ProductController {

    @GetMapping("/list")
    public String productList(
            Model model,
            @PathVariable String product
    ){
        model.addAttribute("text",product);
        return "product";
    }
}
