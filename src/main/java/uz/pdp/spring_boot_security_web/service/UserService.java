package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.spring_boot_security_web.common.exception.RecordAlreadyExist;
import uz.pdp.spring_boot_security_web.common.exception.RecordNotFountException;
import uz.pdp.spring_boot_security_web.config.SecurityConfig;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.model.dto.receive.UserRegisterDTO;
import uz.pdp.spring_boot_security_web.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JavaMailSender javaMailSender;

    public boolean addUser(UserRegisterDTO userRegisterDTO) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(userRegisterDTO.getUsername());
        if (optionalUserEntity.isPresent()) {
            throw new IllegalArgumentException(String.format("username %s already exist", userRegisterDTO.getUsername()));
        }
        UserEntity userEntity = UserEntity.of(userRegisterDTO);
        userEntity.setEmailCode(UUID.randomUUID().toString().substring(0,4));
        userEntity.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
         userRepository.save(userEntity);
        sendEmail(userEntity.getEmail(), userEntity.getEmailCode());

        return true;
    }
    public UserEntity getByUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RecordNotFountException(String.format("user %s not found", username)));
    }

    private void checkByUsername(String username) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        if (userEntity.isPresent()){
            throw new IllegalArgumentException(String.format("username %s already exist", username));
        }
    }
    public void addAdmin(UserRegisterDTO userRegisterDTO){
        checkByUsername(userRegisterDTO.getUsername());
        UserEntity userEntity = UserEntity.of(userRegisterDTO);
        userEntity.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        userRepository.save(userEntity);


    }

    public boolean sendEmail(String email, String emailCode){
       try {
           SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
           simpleMailMessage.setFrom("bekzod@gaiml.com");
           simpleMailMessage.setTo(email);
           simpleMailMessage.setSubject("Keldi kod");
           simpleMailMessage.setText("Assalomu alaykum "+email+" siz ruyxatdan utiz ");
           javaMailSender.send(simpleMailMessage);
           return true;
       }catch (Exception e){
           return false;
       }
    }


    public List<UserEntity> adminList(){
        List<UserEntity> all = userRepository.findAll();
        List<UserEntity>role=new ArrayList<>();
        for (UserEntity userEntity:all){
            if (!userEntity.getRolePermissionEntities().getRoleEnum().equals("USER")){
                role.add(userEntity);
            }
        }
        return role;
    }

    public boolean verifyEmail(String email, String emailCode) {
        Optional<UserEntity> byEmailAndEmailCode = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (byEmailAndEmailCode.isPresent()){
            return true;
        }
        else {
            return false;
        }

    }
}
