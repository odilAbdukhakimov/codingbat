package uz.pdp.spring_boot_security_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.spring_boot_security_web.entity.TestCaseEntity;

import java.util.List;

public interface TestCaseRepository extends JpaRepository<TestCaseEntity, Integer> {
    List<TestCaseEntity> findAllByQuestionId(Integer questionId);
}
