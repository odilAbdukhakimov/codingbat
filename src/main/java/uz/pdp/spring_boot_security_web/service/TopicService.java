package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.spring_boot_security_web.entity.LanguageEntity;
import uz.pdp.spring_boot_security_web.entity.TopicEntity;
import uz.pdp.spring_boot_security_web.model.dto.TopicRequestDTO;
import uz.pdp.spring_boot_security_web.repository.LanguageRepository;
import uz.pdp.spring_boot_security_web.repository.TopicRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;
    private final LanguageRepository languageRepository;

    public List<TopicEntity> getList(String languageName) {
        return languageRepository.findByTitle(languageName).get().getTopicEntities();
    }

    public TopicEntity add(TopicRequestDTO topicRequestDTO) {
        LanguageEntity language = languageRepository.findByTitle(topicRequestDTO.getLanguageName()).get();
        TopicEntity topic = TopicEntity.builder()
                .name(topicRequestDTO.getName())
                .content(topicRequestDTO.getContent())
                .languageEntity(language)
                .build();
//        if (language.getTopicEntities() == null) {
//            language.setTopicEntities(List.of(
//                    topic
//            ));
//        } else {
//            List<TopicEntity> topicEntities = language.getTopicEntities();
//            topicEntities.add(topic);
//            language.setTopicEntities(topicEntities);
//        }
//        languageRepository.save(language);
        return topicRepository.save(topic);
    }

    public void delete(int id) {
        topicRepository.deleteById(id);
    }

    public TopicEntity update(int id, TopicRequestDTO topicRequestDTO) {
        Optional<TopicEntity> byId = topicRepository.findById(id);
        if (byId.isEmpty())
            return null;
        TopicEntity topic = byId.get();
        if (topicRequestDTO.getLanguageName() != null) {
            LanguageEntity language = languageRepository.findByTitle(topicRequestDTO.getLanguageName()).get();
            topic.setLanguageEntity(language);
        }
        if (topicRequestDTO.getContent() != null)
            topic.setContent(topicRequestDTO.getContent());
        if (topicRequestDTO.getName() != null)
            topic.setName(topicRequestDTO.getName());
        return topicRepository.save(topic);
    }

}