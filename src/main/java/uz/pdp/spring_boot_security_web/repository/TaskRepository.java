package uz.pdp.spring_boot_security_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.spring_boot_security_web.entity.TaskEntity;

public interface TaskRepository extends JpaRepository<TaskEntity,Integer> {
}
