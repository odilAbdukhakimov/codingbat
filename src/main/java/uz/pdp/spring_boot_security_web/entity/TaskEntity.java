package uz.pdp.spring_boot_security_web.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.*;
import uz.pdp.spring_boot_security_web.config.AuditListener;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class TaskEntity extends BaseEntity {
    private String name;
    private String title;

}
