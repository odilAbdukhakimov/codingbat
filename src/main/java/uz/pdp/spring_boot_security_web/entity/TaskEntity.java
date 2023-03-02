package uz.pdp.spring_boot_security_web.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class TaskEntity extends BaseEntity {
    private String name;
    private String title;
    private String tickIcon;
    private String example;

    private String methodAndParams;

}
