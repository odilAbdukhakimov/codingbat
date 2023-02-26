package uz.pdp.spring_boot_security_web.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.spring_boot_security_web.common.exception.RecordNotFountException;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.model.dto.AdminRequestDto;
import uz.pdp.spring_boot_security_web.model.dto.receive.UserRegisterDTO;
import uz.pdp.spring_boot_security_web.repository.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthenticationManager authenticationManager;
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
//    public UserEntity getByUser(String username) {
//        return userRepository.findByUsername(username).orElseThrow(() -> new RecordNotFountException(String.format("user %s not found", username)));
//    }

    public void update(String name, UserRegisterDTO userRegisterDTO) {
        Optional<UserEntity> byUsername = userRepository.findByUsername(name);
        RolePermissionEntity rolePermission=new RolePermissionEntity();
        if (byUsername.isPresent()){
            UserEntity oldUserEntity = byUsername.get();
            if (userRegisterDTO.getName()!=null && !userRegisterDTO.getName().equals("")){
                oldUserEntity.setName(userRegisterDTO.getName());
            }
            if (userRegisterDTO.getPassword()!=null){
                oldUserEntity.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
            }
            if (userRegisterDTO.getPermissions()!=null){
                rolePermission.setPermissionEnum(userRegisterDTO.getPermissions());
                oldUserEntity.setRolePermissionEntities(rolePermission);
            }
            if (userRegisterDTO.getRole()!=null){
                rolePermission.setRoleEnum(userRegisterDTO.getRole());
                oldUserEntity.setRolePermissionEntities(rolePermission);
            }
            if (userRegisterDTO.getUsername()!=null && !userRegisterDTO.getUsername().equals("")){
                oldUserEntity.setUsername(userRegisterDTO.getUsername());
            }
            userRepository.save(oldUserEntity);
        }
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
    public void addAdmin(AdminRequestDto adminRequestDto){
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
        if (byEmailAndEmailCode.isPresent()){
            return true;
        }
        else {
            return false;
        }

    }

    public List<UserEntity> adminEntityList(){
        List<UserEntity> userList = userRepository.findAll();
        List<UserEntity> adminList = new ArrayList<>();
        for (UserEntity userEntity : userList) {
            if (userEntity.getRolePermissionEntities().getRoleEnum().contains("ADMIN")) {

                adminList.add(userEntity);
            }
        }
        return  adminList;
    }
    public boolean resetPassword(String email){
        try {
            SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
            simpleMailMessage.setFrom("bekzod@gaiml.com");
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("Keldi kod");
simpleMailMessage.setText("<a href='http://localhost:8080/api/user/password/reset/email="+email+"'>hello</a>");
            javaMailSender.send(simpleMailMessage);
            return true;
        }catch (Exception e){
            return false;
        }
    }

public UserEntity findEmail(String email){
return userRepository.findByEmail(email);

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
    public UserEntity getCurrentUser() {
        Object currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser !=null){
            try {
                UserEntity user= (UserEntity) currentUser;
                System.out.println(user.getUsername());
                return user;
            }catch (Exception e){
                return new UserEntity("","","","",null);
            }
        }
        return null;
    }
    public UserEntity updateUser(String username, UserRegisterDTO userRegisterDTO) {
        UserEntity byUsername = getByUser(username);
        RolePermissionEntity rolePermission=new RolePermissionEntity();

        if (userRegisterDTO.getName()!=null && !userRegisterDTO.getName().equals("")){
            byUsername.setName(userRegisterDTO.getName());
        }
        if (userRegisterDTO.getPassword()!=null){
            byUsername.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        }
        if (userRegisterDTO.getPermissions()!=null){
            rolePermission.setPermissionEnum(userRegisterDTO.getPermissions());
            byUsername.setRolePermissionEntities(rolePermission);
        }
        if (userRegisterDTO.getRole()!=null){
            rolePermission.setRoleEnum(userRegisterDTO.getRole());
            byUsername.setRolePermissionEntities(rolePermission);
        }else {
            rolePermission.setRoleEnum(List.of(RoleEnum.USER.name()));
        }
        if (userRegisterDTO.getUsername()!=null && !userRegisterDTO.getUsername().equals("")){
            byUsername.setUsername(userRegisterDTO.getUsername());
        }
        return userRepository.save(byUsername);
    }
    public void updateUserContextHolder(UserEntity currentUser, HttpServletRequest request) {
        RolePermissionEntity rolePermission = new RolePermissionEntity();
        rolePermission.setRoleEnum(List.of(RoleEnum.USER.name()));
        currentUser.setRolePermissionEntities(rolePermission);
//        UsernamePasswordAuthenticationToken authToken=
//                new UsernamePasswordAuthenticationToken(
//                        currentUser,SecurityContextHolder.getContext().getAuthentication().getCredentials(),currentUser.getAuthorities());
//        Authentication authenticate = authenticationManager.authenticate(authToken);
//        SecurityContext context = SecurityContextHolder.getContext();
//        context.setAuthentication(authenticate);

        Authentication authToken=
                new UsernamePasswordAuthenticationToken(
                        currentUser,SecurityContextHolder.getContext().getAuthentication().getCredentials(),currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
