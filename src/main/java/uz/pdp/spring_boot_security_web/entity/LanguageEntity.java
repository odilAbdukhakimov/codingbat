package uz.pdp.spring_boot_security_web.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.pdp.spring_boot_security_web.config.AuditListener;
import uz.pdp.spring_boot_security_web.model.dto.LanguageRequestDTO;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class LanguageEntity extends BaseEntity {
    @Column(unique = true)
    private String title;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "languageEntity", cascade = CascadeType.ALL)
    private List<TopicEntity> topicEntities;

    public LanguageEntity of(LanguageRequestDTO languageRequestDTO) {
        return LanguageEntity.builder()
                .title(languageRequestDTO.getTitle())
                .build();
    }
}
