package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.spring_boot_security_web.common.exception.RecordNotFountException;
import uz.pdp.spring_boot_security_web.entity.AttachmentContentEntity;
import uz.pdp.spring_boot_security_web.entity.AttachmentEntity;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.repository.AttachmentContentRepository;
import uz.pdp.spring_boot_security_web.repository.AttachmentRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final AttachmentRepository attachmentRepository;
    private final AttachmentContentRepository attachmentContentRepository;
    private static final String getPath = "../static/";
    private static final String uploadPath2 = "C:\\Users\\odila\\IdeaProjects\\codingbat\\src\\main\\resources\\static\\images";


    @SneakyThrows
    public String uploadImage2(MultipartFile file )  {

        if (file != null) {
            String originalFileName = file.getOriginalFilename();
            long size = file.getSize();
            String contentType = file.getContentType();

            AttachmentEntity attachment = new AttachmentEntity();
            attachment.setSize(size);
            attachment.setContentType(contentType);
            attachment.setFileOriginalName(originalFileName);

            String randomName = makeRandomFileName(originalFileName);

            attachment.setName(randomName);
            AttachmentEntity saveAttachment = attachmentRepository.save(attachment);

            AttachmentContentEntity attachmentContentEntity =new AttachmentContentEntity();
            attachmentContentEntity.setAttachment(saveAttachment);
            attachmentContentEntity.setBytes(file.getBytes());
            attachmentContentRepository.save(attachmentContentEntity);

            writeToFile(file,randomName);
            return randomName;
        }
        throw new IOException("file  not found");
    }
    private String makeRandomFileName(String originalFileName ){
        String[] split = originalFileName.split("\\.");
        return UUID.randomUUID().toString() + "." + split[split.length - 1];
    }

    @SneakyThrows
    private void writeToFile(MultipartFile file, String randomName){
        Path path = Paths.get(uploadPath2 + "/" + randomName);
        Files.copy(file.getInputStream(), path);
    }
    @SneakyThrows
    public void updateImage(MultipartFile file , String url){
        if (url!=null){
            Optional<AttachmentEntity> foundByName = attachmentRepository.findByName(url);
            if (foundByName.isPresent()){
                String randomName = makeRandomFileName(file.getOriginalFilename());
                writeToFile(file,randomName);

                AttachmentEntity attachmentEntity = foundByName.get();
                attachmentEntity.setName(randomName);
                attachmentEntity.setSize(file.getSize());
                attachmentEntity.setContentType(file.getContentType());
                attachmentEntity.setFileOriginalName(file.getOriginalFilename());

                AttachmentEntity updateAttachment = attachmentRepository.save(attachmentEntity);
                Optional<AttachmentContentEntity> foundByAtmId = attachmentContentRepository.findByAttachmentId(updateAttachment.getId());
                if (foundByAtmId.isPresent()){
                    AttachmentContentEntity attachmentContentEntity = foundByAtmId.get();
                    attachmentContentEntity.setBytes(file.getBytes());
                    attachmentContentEntity.setAttachment(updateAttachment);
                    attachmentContentRepository.save(attachmentContentEntity);
                }else {
                    throw new RecordNotFountException("AttachmentContentEntity was not found");
                }
                File removedFile= new File(uploadPath2+"/"+url);
                if (removedFile.delete()){
                    System.out.println("File was  deleted successfully");
                }else {
                    System.out.println("File was Not  deleted successfully");
                }
            }else {
                throw new RecordNotFountException("AttachmentEntity was not found");
            }
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
