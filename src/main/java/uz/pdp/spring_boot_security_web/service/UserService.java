package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.spring_boot_security_web.common.exception.RecordNotFountException;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.entity.role.RoleEnum;
import uz.pdp.spring_boot_security_web.entity.role.RolePermissionEntity;
import uz.pdp.spring_boot_security_web.model.dto.AdminRequestDto;
import uz.pdp.spring_boot_security_web.model.dto.receive.UserRegisterDTO;
import uz.pdp.spring_boot_security_web.repository.UserRepository;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ImageService imageService;

    public boolean addUser(UserRegisterDTO userRegisterDTO) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(userRegisterDTO.getUsername());
        if (optionalUserEntity.isPresent()) {
            throw new IllegalArgumentException(String.format("username %s already exist", userRegisterDTO.getUsername()));
        }
        UserEntity userEntity = UserEntity.of(userRegisterDTO);
        userEntity.setEmailCode(UUID.randomUUID().toString().substring(0, 4));
        userEntity.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        String url = imageService.uploadImage2(userRegisterDTO.getImage());
        userEntity.setLogoUrl(url);
        userRepository.save(userEntity);
        emailService.sendMessage(userEntity.getEmail(), "Hi! " +
                "Your " + userEntity.getEmail() + " account has been successfully registered");
        return true;
    }

    public UserEntity getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RecordNotFountException(String.format("user %s not found", username)));
    }

    public void addAdmin(AdminRequestDto adminRequestDto) {
        UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
                .name(adminRequestDto.getName())
                .username(adminRequestDto.getUsername())
                .permissions(Arrays.stream(adminRequestDto.getPermission().split(",")).toList())
                .role(Arrays.stream(adminRequestDto.getRoles().split(",")).toList())
                .build();
        UserEntity userEntity = UserEntity.of(userRegisterDTO);
        userEntity.setPassword(passwordEncoder.encode(adminRequestDto.getPassword()));
        userRepository.save(userEntity);
    }

    public List<UserEntity> getAdminList() {
        List<UserEntity> userList = userRepository.findAll();
        List<UserEntity> adminList = new ArrayList<>();
        for (UserEntity userEntity : userList) {
            if (userEntity.getRolePermissionEntities().getRoleEnum().contains("ADMIN")) {

                adminList.add(userEntity);
            }
        }
        return adminList;
    }

    public void update(UserEntity byUser) {
        userRepository.save(byUser);
    }

    public UserEntity findByEmail(String email) {
        //return userRepository.findByEmail(email).orElse(null);
        Optional<UserEntity> getUserByEmail = userRepository.findByEmail(email);
        System.out.println(email.substring(0, email.lastIndexOf(".")));
        if (getUserByEmail.isPresent()){
            return  getUserByEmail.get();
        }
        return null;
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
        if (adminRequestDto.getPermission() != null) {
            rolePermission.setPermissionEnum(
                    Arrays.stream(adminRequestDto.getRoles().split(",")).toList()
            );
        }
        userRepository.save(userEntity);
    }

    public UserEntity getCurrentUser() {
        Object currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser != null) {
            try {
                UserEntity user = (UserEntity) currentUser;
                return user;
            } catch (Exception e) {
                return new UserEntity("", "", "", "", null);
            }
        }
        return null;
    }

    public UserEntity updateUser(String username, UserRegisterDTO userRegisterDTO) {
        UserEntity byUsername = getByUsername(username);
        RolePermissionEntity rolePermission = new RolePermissionEntity();

        if (userRegisterDTO.getName() != null && !userRegisterDTO.getName().equals("")) {
            byUsername.setName(userRegisterDTO.getName());
        }
        if (userRegisterDTO.getPassword() != null) {
            byUsername.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        }
        if (userRegisterDTO.getPermissions() != null) {
            rolePermission.setPermissionEnum(userRegisterDTO.getPermissions());
            byUsername.setRolePermissionEntities(rolePermission);
        }
        if (userRegisterDTO.getRole() != null) {
            rolePermission.setRoleEnum(userRegisterDTO.getRole());
            byUsername.setRolePermissionEntities(rolePermission);
        } else {
            rolePermission.setRoleEnum(List.of(RoleEnum.USER.name()));
        }
        if (userRegisterDTO.getUsername() != null && !userRegisterDTO.getUsername().equals("")) {
            byUsername.setUsername(userRegisterDTO.getUsername());
        }
        if (userRegisterDTO.getImage()!=null){
            byUsername.setLogoUrl(imageService.updateImage(userRegisterDTO.getImage(), byUsername.getLogoUrl()));
        }
        return userRepository.save(byUsername);
    }

    public void updateUserContextHolder(UserEntity currentUser) {
        RolePermissionEntity rolePermission = new RolePermissionEntity();
        rolePermission.setRoleEnum(List.of(RoleEnum.USER.name()));
        currentUser.setRolePermissionEntities(rolePermission);
        Authentication authToken =
                new UsernamePasswordAuthenticationToken(
                        currentUser, SecurityContextHolder.getContext().getAuthentication().getCredentials(), currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    public String getAdmin_In_Roles(UserEntity userEntity) {
        List<String> roleEnum = userEntity.getRolePermissionEntities().getRoleEnum();
        for (String admin : roleEnum) {
            if (admin.equals("ADMIN") || admin.equals("SUPER_ADMIN")) {
                return admin;
            }
        }
        return "";
    }
}
