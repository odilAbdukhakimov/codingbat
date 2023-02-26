package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.spring_boot_security_web.common.exception.RecordNotFountException;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.entity.role.RolePermissionEntity;
import uz.pdp.spring_boot_security_web.model.dto.AdminRequestDto;
import uz.pdp.spring_boot_security_web.model.dto.receive.UserRegisterDTO;
import uz.pdp.spring_boot_security_web.repository.UserRepository;

import java.util.*;

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
        userEntity.setEmailCode(UUID.randomUUID().toString().substring(0, 4));
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
        if (userEntity.isPresent()) {
            throw new IllegalArgumentException(String.format("username %s already exist", username));
        }
    }

    public void addAdmin(AdminRequestDto adminRequestDto) {
        UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
                .name(adminRequestDto.getName())
                .username(adminRequestDto.getUsername())
                .permissions(Arrays.stream(adminRequestDto.getPermission().split(",")).toList())
                .role(Arrays.stream(adminRequestDto.getRoles().split(",")).toList())
                .build();
        //checkByUsername(adminRequestDto.getUsername());
        UserEntity userEntity = UserEntity.of(userRegisterDTO);
        userEntity.setPassword(passwordEncoder.encode(adminRequestDto.getPassword()));
        userRepository.save(userEntity);

    }


    public boolean sendEmail(String email, String emailCode) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("bekzod@gaiml.com");
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("Keldi kod");
            simpleMailMessage.setText("Assalomu alaykum " + email + " siz ruyxatdan utiz ");
            javaMailSender.send(simpleMailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


//    public List<UserEntity> adminList(){
//        List<UserEntity> all = userRepository.findAll();
//        List<UserEntity>role=new ArrayList<>();
//        for (UserEntity userEntity:all){
//            if (!userEntity.getRolePermissionEntities().getRoleEnum().equals("USER")){
//                role.add(userEntity);
//            }
//        }
//        return role;
//    }

    public boolean verifyEmail(String email, String emailCode) {
        Optional<UserEntity> byEmailAndEmailCode = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (byEmailAndEmailCode.isPresent()) {
            return true;
        } else {
            return false;
        }

    }

    public List<UserEntity> adminEntityList() {
        List<UserEntity> userList = userRepository.findAll();
        List<UserEntity> adminList = new ArrayList<>();
        for (UserEntity userEntity : userList) {
            if (userEntity.getRolePermissionEntities().getRoleEnum().contains("ADMIN")) {

                adminList.add(userEntity);
            }
        }
        return adminList;
    }

    public void delete(int id) {
        Optional<UserEntity> byId = userRepository.findById(id);
        if (byId.isEmpty())
            throw new RecordNotFountException("User note found");
        UserEntity userEntity = byId.get();
        userRepository.delete(userEntity);
    }

    public void updateAdmin(int adminId, AdminRequestDto adminRequestDto) {
        Optional<UserEntity> byId = userRepository.findById(adminId);
        if (byId.isEmpty())
            throw new RecordNotFountException("This user not found");
        UserEntity userEntity = byId.get();
        RolePermissionEntity rolePermission = userEntity.getRolePermissionEntities();
        if (adminRequestDto.getName() != null)
            userEntity.setName(adminRequestDto.getName());
        if (adminRequestDto.getUsername() != null)
            userEntity.setUsername(adminRequestDto.getUsername());
        if (adminRequestDto.getPassword() != null)
            userEntity.setPassword(adminRequestDto.getPassword());
        if (adminRequestDto.getRoles() != null) {
            rolePermission.setRoleEnum(
                    Arrays.stream(adminRequestDto.getRoles().split(",")).toList()
            );
        }
        if (adminRequestDto.getPermission() != null){
            rolePermission.setPermissionEnum(
                    Arrays.stream(adminRequestDto.getRoles().split(",")).toList()
            );
        }
        userRepository.save(userEntity);
    }
}
