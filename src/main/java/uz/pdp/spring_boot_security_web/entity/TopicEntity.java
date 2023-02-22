package uz.pdp.spring_boot_security_web.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.spring_boot_security_web.config.AuditListener;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@Builder
public class TopicEntity extends BaseEntity {

    private String name;
    private String content;
    @ManyToOne
    private LanguageEntity languageEntity;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<TaskEntity> taskEntityList;

}
