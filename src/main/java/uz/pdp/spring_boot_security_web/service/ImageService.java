package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.spring_boot_security_web.entity.AttachmentEntity;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.repository.AttachmentRepository;
import uz.pdp.spring_boot_security_web.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final AttachmentRepository attachmentRepository;
    private final UserService userService;

    private static final String uploadPath = "downloadPictures";
    private static final String getPath = "../static/";
    private static final String uploadPath2 = "C:/Users/odila/IdeaProjects/codingbat/src/main/resources/static";


    public void uploadImage(MultipartFile file ,String username) throws IOException {

        if (file != null) {
            String originalFileName = file.getOriginalFilename();
            long size = file.getSize();
            String contentType = file.getContentType();

            AttachmentEntity attachment = new AttachmentEntity();
            attachment.setSize(size);
            attachment.setContentType(contentType);
            attachment.setFileOriginalName(originalFileName);

            String[] split = originalFileName.split("\\.");
            String randomName = UUID.randomUUID().toString() + "." + split[split.length - 1];

            attachment.setName(randomName);
            attachmentRepository.save(attachment);
            UserEntity byUser = userService.getByUsername(username);
            byUser.setLogoUrl(randomName);
            userService.update(byUser);
            Path path = Paths.get(uploadPath2 + "/" + randomName);
            Files.copy(file.getInputStream(), path);
        }
    }

    public String getPicture(int id) {
        Optional<AttachmentEntity> optionalAttachment = attachmentRepository.findById(id);
        if (optionalAttachment.isPresent()) {
            AttachmentEntity attachment = optionalAttachment.get();
            return getPath + attachment.getName();
        }
        return null;
    }
}
