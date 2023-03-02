package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.spring_boot_security_web.common.exception.RecordNotFountException;
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
        Optional<LanguageEntity> byTitle = languageRepository.findByTitle(languageName);
        if (byTitle.isEmpty()) return null;
        return languageRepository.findByTitle(languageName).get().getTopicEntities();
    }

    public TopicEntity add(TopicRequestDTO topicRequestDTO) {
        Optional<LanguageEntity> languageOptional = languageRepository.findByTitle(topicRequestDTO.getLanguage());
        if (languageOptional.isEmpty()) {
            return null;
        }
        TopicEntity topic = TopicEntity.builder()
                .name(topicRequestDTO.getName())
                .content(topicRequestDTO.getContent())
                .languageEntity(languageOptional.get())
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
        Optional<TopicEntity> byId = topicRepository.findById(id);
        if (byId.isEmpty()){
            throw new RecordNotFountException("The topic is not found");

        }
        topicRepository.deleteById(id);
    }

    public TopicEntity update(int id, TopicRequestDTO topicRequestDTO) {
        Optional<TopicEntity> byId = topicRepository.findById(id);
        if (byId.isEmpty())
            return null;
        TopicEntity topic = byId.get();
//        if (topicRequestDTO.getLanguageId() != null) {
//            Optional<LanguageEntity> byId1 = languageRepository.findById(topicRequestDTO.getLanguageId());
//            byId1.ifPresent(topic::setLanguageEntity);
//        }
        if (topicRequestDTO.getContent() != null)
            topic.setContent(topicRequestDTO.getContent());
        if (topicRequestDTO.getName() != null)
            topic.setName(topicRequestDTO.getName());
        return topicRepository.save(topic);
    }

    public TopicEntity getById(int id) {
        Optional<TopicEntity> byId = topicRepository.findById(id);
        return byId.orElse(null);
    }
}
