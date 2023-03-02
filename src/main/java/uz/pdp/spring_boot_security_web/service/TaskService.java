package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.spring_boot_security_web.common.exception.RecordNotFountException;
import uz.pdp.spring_boot_security_web.entity.LanguageEntity;
import uz.pdp.spring_boot_security_web.entity.TaskEntity;
import uz.pdp.spring_boot_security_web.entity.TopicEntity;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.model.dto.TaskRequestDTO;
import uz.pdp.spring_boot_security_web.repository.LanguageRepository;
import uz.pdp.spring_boot_security_web.repository.TaskRepository;
import uz.pdp.spring_boot_security_web.repository.TopicRepository;
import uz.pdp.spring_boot_security_web.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TopicRepository topicRepository;
    private final LanguageRepository languageRepository;
    private final UserRepository userRepository;

    public TaskEntity addTask(TaskRequestDTO taskRequestDTO) {
        Optional<TopicEntity> byId = topicRepository.findById(taskRequestDTO.getTopicId());
        if (byId.isEmpty()) return null;
        TopicEntity topic = byId.get();
        TaskEntity taskEntity = TaskEntity.builder()
                .name(taskRequestDTO.getName())
                .title(taskRequestDTO.getTitle())
                .build();
        if (topic.getTaskEntityList() == null){
            topic.setTaskEntityList(List.of(
                    taskEntity
            ));
        } else {
            List<TaskEntity> taskEntityList = topic.getTaskEntityList();
            taskEntityList.add(taskEntity);
            topic.setTaskEntityList(taskEntityList);
        }
        topicRepository.save(topic);
        return taskRepository.save(taskEntity);
    }

    public void delete(int taskId) {
        Optional<TaskEntity> byId = taskRepository.findById(taskId);
        if (byId.isEmpty()){
            throw new RecordNotFountException("The task is not found");
        }
        TaskEntity taskEntity = byId.get();
        taskRepository.delete(taskEntity);

    }
    public List<TaskEntity> getTaskList(int topicId){
        Optional<TopicEntity> byId = topicRepository.findById(topicId);
        return byId.get().getTaskEntityList();
    }

    public TaskEntity update(int id, TaskRequestDTO taskRequestDTO) {
        Optional<TaskEntity> byId = taskRepository.findById(id);
        if (byId.isEmpty())
            return null;
        TaskEntity taskEntity = byId.get();
        if (taskRequestDTO.getName() != null)
            taskEntity.setName(taskRequestDTO.getName());
        if (taskRequestDTO.getTitle() != null)
            taskEntity.setTitle(taskRequestDTO.getTitle());
        return taskRepository.save(taskEntity);

    }

    public TaskEntity getById(int id){
        Optional<TaskEntity> byId = taskRepository.findById(id);
        return byId.orElse(null);
    }
    public List<TaskEntity> getTaskListByTopicAndLanguage(String language, String topic){
        Optional<LanguageEntity> byTitle = languageRepository.findByTitle(language);
        if (byTitle.isEmpty()) return null;
        List<TopicEntity> topicEntities = byTitle.get().getTopicEntities();
        Optional<TopicEntity> first = topicEntities.stream().filter((s) -> s.getName().equals(topic)).findFirst();
        return first.map(TopicEntity::getTaskEntityList).orElse(null);
    }

    public List<TaskEntity> getTasksUserSolvedAndNotSolved(UserEntity user, String language, String topic) {

        List<TaskEntity> taskListByTopicAndLanguage = getTaskListByTopicAndLanguage(language, topic);

        if (user.getUsername() != null){

            List<TaskEntity> userTaskEntityList = getUserTaskEntityList(user.getUsername());

            if (userTaskEntityList != null && taskListByTopicAndLanguage != null) {
                for (TaskEntity taskEntity : taskListByTopicAndLanguage) {
                    for (TaskEntity entity : userTaskEntityList) {
                        if (taskEntity.getName().equals(entity.getName())){
                            taskEntity.setTickIcon("✅");
                        }
                    }
                }
            }
        }

        return taskListByTopicAndLanguage;
    }
    public List<TaskEntity> getUserTaskEntityList(String username) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        return userEntity.map(UserEntity::getTaskEntityList).orElse(null);
    }

//-------------------------------`---------------------------------

    public String getTopicNameByQuestion(TaskEntity question, List<LanguageEntity> list){
        for (LanguageEntity subjectEntity : list) {
            for (TopicEntity topicEntity : subjectEntity.getTopicEntities()) {
                for (TaskEntity questionEntity : topicEntity.getTaskEntityList()) {
                    if(questionEntity.equals(question)){
                        return topicEntity.getName();
                    }
                }
            }
        }
        return null;
    }


    public void makeQuestionSolved(UserEntity user, TaskEntity question){
        List<TaskEntity> questionEntityList = user.getTaskEntityList();
        questionEntityList.add(question);
        user.setTaskEntityList(questionEntityList);
        try {
            userRepository.save(user);
        }catch (Exception e){
            System.err.println("Trying to solve the same question second time");
            e.printStackTrace();
        }
    }



}
