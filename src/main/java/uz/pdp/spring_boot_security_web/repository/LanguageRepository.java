package uz.pdp.spring_boot_security_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.spring_boot_security_web.entity.LanguageEntity;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<LanguageEntity, Integer> {

    Optional<LanguageEntity> findByTitle(String title);
}
