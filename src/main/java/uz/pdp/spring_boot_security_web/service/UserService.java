package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.spring_boot_security_web.common.exception.RecordAlreadyExist;
import uz.pdp.spring_boot_security_web.common.exception.RecordNotFountException;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.entity.role.RolePermissionEntity;
import uz.pdp.spring_boot_security_web.model.dto.receive.UserRegisterDTO;
import uz.pdp.spring_boot_security_web.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
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
            if (userRegisterDTO.getUsername()!=null){
                oldUserEntity.setUsername(userRegisterDTO.getUsername());
            }
            userRepository.save(oldUserEntity);
        }
    }
    public UserEntity getByUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RecordNotFountException(String.format("user %s not found", username)));
    }

    private void checkByUsername(String username) {
        UserEntity userEntity = getByUser(username);
        for (String role : userEntity.getRolePermissionEntities().getRoleEnum()) {
            if (role.equals("ADMIN")){
                throw new RecordAlreadyExist(String.format("username %s already exists", username));
            }
        }
    }
    public void addAdmin(UserRegisterDTO userRegisterDTO){
        checkByUsername(userRegisterDTO.getUsername());
        UserEntity userEntity = UserEntity.of(userRegisterDTO);
        userEntity.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        userRepository.save(userEntity);

    }

    public List<UserEntity> userEntityList(){
        return userRepository.findAll();
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
}
