package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.spring_boot_security_web.entity.TaskEntity;
import uz.pdp.spring_boot_security_web.entity.TopicEntity;
import uz.pdp.spring_boot_security_web.model.dto.TaskRequestDTO;
import uz.pdp.spring_boot_security_web.repository.TaskRepository;
import uz.pdp.spring_boot_security_web.repository.TopicRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TopicRepository topicRepository;

    public TaskEntity addTask(TaskRequestDTO taskRequestDTO) {
        TopicEntity topic = topicRepository.findById(taskRequestDTO.getTopicId()).get();
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
        taskRepository.deleteById(taskId);
    }
    public List<TaskEntity> getTaskList(int topicId){
        Optional<TopicEntity> byId = topicRepository.findById(topicId);
        return byId.get().getTaskEntityList();
    }

    public TaskEntity update(int id, TaskRequestDTO taskRequestDTO) {
        TaskEntity taskEntity = taskRepository.findById(id).get();
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
}