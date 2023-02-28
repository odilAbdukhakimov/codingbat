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
    private final UserRepository userRepository;
    private final LanguageRepository languageRepository;

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
        if (byId.isEmpty()){
            return null;
        }
        return byId.get();
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

            if (!userTaskEntityList.isEmpty()) {
                for (TaskEntity taskEntity : taskListByTopicAndLanguage) {
                    for (TaskEntity entity : userTaskEntityList) {
                        if (taskEntity.getName().equals(entity.getName())){
                            taskEntity.setIsSolved("✅");
                        }
                    }
                }
            }
        }
        for (TaskEntity taskEntity : taskListByTopicAndLanguage) {
            System.out.println("taskEntity = " + taskEntity.getIsSolved());
        }

        return taskListByTopicAndLanguage;
    }
    public List<TaskEntity> getUserTaskEntityList(String username) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        return userEntity.map(UserEntity::getTaskEntityList).orElse(null);
    }
}
