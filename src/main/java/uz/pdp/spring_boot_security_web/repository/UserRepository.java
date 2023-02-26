package uz.pdp.spring_boot_security_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.spring_boot_security_web.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity>findByEmailAndEmailCode(String email, String emailCode);
    Optional<List<UserEntity>> findByAndRolePermissionEntities_RoleEnum(String role);
}
