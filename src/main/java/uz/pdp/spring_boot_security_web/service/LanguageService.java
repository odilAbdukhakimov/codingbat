package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.spring_boot_security_web.common.exception.RecordNotFountException;
import uz.pdp.spring_boot_security_web.entity.LanguageEntity;
import uz.pdp.spring_boot_security_web.model.dto.LanguageRequestDTO;
import uz.pdp.spring_boot_security_web.repository.LanguageRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LanguageService {
    private final LanguageRepository languageRepository;

    public List<LanguageEntity> languageEntityList() {
        return languageRepository.findAll();
    }

    public LanguageEntity getLanguage(String name) {
        Optional<LanguageEntity> optionalSubjectEntity = languageRepository.findByTitle(name);
        return optionalSubjectEntity.orElse(null);
    }

    public LanguageEntity addLanguage(LanguageRequestDTO languageRequestDTO) {
        Optional<LanguageEntity> optional = languageRepository.findByTitle(languageRequestDTO.getTitle());
        if (optional.isPresent()) {
            throw new IllegalArgumentException(String.format("The %s already exist", languageRequestDTO.getTitle()));
        }
        LanguageEntity language = LanguageEntity.builder()
                .title(languageRequestDTO.getTitle())
                .build();

        return languageRepository.save(language);
    }

    public void delete(int id) {
        Optional<LanguageEntity> optional = languageRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RecordNotFountException("The language is not found");
        }
        languageRepository.deleteById(id);
    }

    public LanguageEntity update(int id, LanguageRequestDTO newLanguageRequestDTO) {
        Optional<LanguageEntity> byTitle = languageRepository.findById(id);
        if (byTitle.isEmpty()) {
            throw new RecordNotFountException("The language is not found");
        }
        LanguageEntity language = byTitle.get();
        language.setTitle(newLanguageRequestDTO.getTitle());
        return languageRepository.save(language);
    }

    public LanguageEntity getById(int id) {
        Optional<LanguageEntity> byId = languageRepository.findById(id);
        return byId.orElse(null);
    }
}
