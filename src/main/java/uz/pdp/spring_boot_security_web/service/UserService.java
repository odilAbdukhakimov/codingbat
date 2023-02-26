package uz.pdp.spring_boot_security_web.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.spring_boot_security_web.common.exception.RecordNotFountException;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.entity.role.RoleEnum;
import uz.pdp.spring_boot_security_web.entity.role.RolePermissionEntity;
import uz.pdp.spring_boot_security_web.model.dto.receive.UserRegisterDTO;
import uz.pdp.spring_boot_security_web.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public boolean addUser(UserRegisterDTO userRegisterDTO) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(userRegisterDTO.getUsername());
        if (optionalUserEntity.isPresent()) {
            throw new IllegalArgumentException(String.format("username %s already exist", userRegisterDTO.getUsername()));
        }
        UserEntity userEntity = UserEntity.of(userRegisterDTO);
        userEntity.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        UserEntity save = userRepository.save(userEntity);
        System.out.println(save.getName());

        return true;
    }

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
    public void addAdmin(UserRegisterDTO userRegisterDTO){
        checkByUsername(userRegisterDTO.getUsername());
        UserEntity userEntity = UserEntity.of(userRegisterDTO);
        userEntity.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        userRepository.save(userEntity);

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

    public List<UserEntity> getALlAdmins() {
        List<UserEntity> all = userRepository.findAll();
        List<UserEntity> admins= new ArrayList<>();
        for (UserEntity userEntity :all){
            if(userEntity.getRolePermissionEntities() !=null){
                List<String> roleEnum = userEntity.getRolePermissionEntities().getRoleEnum();
                for (String role:roleEnum){
                    if (role.equals("ADMIN")) {
                        admins.add(userEntity);
                        break;
                    }
                }
            }
        }
        return admins;
    }

    public void delete(String username) {
        Optional<UserEntity> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()){
            userRepository.delete(byUsername.get());
        }else {
            throw new RecordNotFountException(String.format("username %s not found in  database",username));
        }
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

    public void update(UserEntity byUser) {
        userRepository.save(byUser);
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
        UsernamePasswordAuthenticationToken authToken=
                new UsernamePasswordAuthenticationToken(
                        currentUser,SecurityContextHolder.getContext().getAuthentication().getCredentials(),currentUser.getAuthorities());
        Authentication authenticate = authenticationManager.authenticate(authToken);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authenticate);
    }
}
