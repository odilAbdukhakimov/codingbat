package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.entity.role.RolePermissionEntity;
import uz.pdp.spring_boot_security_web.model.dto.receive.UserRegisterDTO;
import uz.pdp.spring_boot_security_web.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

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
            if (userRegisterDTO.getName()!=null){
                oldUserEntity.setName(userRegisterDTO.getName());
            }
            if (userRegisterDTO.getPassword()!=null){
                oldUserEntity.setPassword(userRegisterDTO.getPassword());
            }
            if (userRegisterDTO.getPermissions()!=null){
                rolePermission.setPermissionEnum(userRegisterDTO.getPermissions());
                oldUserEntity.setRolePermissionEntities(rolePermission);
            }
            if (userRegisterDTO.getRole()!=null){
                rolePermission.setRoleEnum(userRegisterDTO.getRole());
                oldUserEntity.setRolePermissionEntities(rolePermission);
            }
            if (userRegisterDTO.getUsername()!=null){
                oldUserEntity.setUsername(userRegisterDTO.getUsername());
            }
            userRepository.save(oldUserEntity);
        }
    }
}
