package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.spring_boot_security_web.entity.AttachmentEntity;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.repository.AttachmentRepository;
import uz.pdp.spring_boot_security_web.service.ImageService;
import uz.pdp.spring_boot_security_web.service.UserService;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/api/picture/")
@RequiredArgsConstructor
public class PictureController {
    private final ImageService imageService;
    private final UserService userService;
    private static final String uploadPath = "downloadPictures";
    private static final String getPath = "../static/";
    private static final String uploadPath2 = "D:/Spring-framwork/codingbat/src/main/resources/static";

    @PostMapping("/uploadSystem")
    public String upload(MultipartHttpServletRequest request) {
        System.out.println("hello");
        return "task";
    }

    //    @RequestParam("file") MultipartFile file
    @PostMapping("/upload/{username}")
    public String uploadFileSystem( @PathVariable("username") String username,
                                    MultipartHttpServletRequest request,
                                    Model model) throws IOException {

        Iterator<String> fileName = request.getFileNames();
        imageService.uploadImage(request.getFile(fileName.next()),username);
        UserEntity currentUser = userService.getCurrentUser();
        if (currentUser!=null){
            model.addAttribute("currentUser",currentUser);
        }
        return "index";
    }

    @GetMapping("/show")
    public String showAddPicture( Model model) {
        UserEntity currentUser = userService.getCurrentUser();
        if (currentUser!=null){
            model.addAttribute("currentUser",currentUser);
        }
        return "picture";
    }

    @SneakyThrows
    @GetMapping("/get/{id}")
    public String getPictureById(@PathVariable("id") int id, Model model) {

        String picture = imageService.getPicture(id);

        model.addAttribute("photo", picture);
        return "CrudAdmin";
    }
}
