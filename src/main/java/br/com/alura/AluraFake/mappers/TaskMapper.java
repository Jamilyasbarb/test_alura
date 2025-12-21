package br.com.alura.AluraFake.mappers;

import br.com.alura.AluraFake.domain.Course;
import br.com.alura.AluraFake.domain.Task;
import br.com.alura.AluraFake.domain.enums.TaskType;
import br.com.alura.AluraFake.dto.task.CreateTaskDTO;
import br.com.alura.AluraFake.dto.task.TaskDTO;
import br.com.alura.AluraFake.dto.task.TaskOptionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    @Autowired
    private CourseMapper courseMapper;


    public Task toEntityFromCreateDTO(CreateTaskDTO createTaskDTO, Course course, TaskType type){
        return new Task(
                null,
                createTaskDTO.statement(),
                createTaskDTO.order(),
                course,
                type
        );
    }

    public TaskDTO toTaskDTOFromEntity(Task task){
        return new TaskDTO(
                task.getId(),
                task.getStatement(),
                task.getOrder(),
                courseMapper.toCourseDTOFromEntity(task.getCourse()),
                task.getTaskOptions().stream().map(option -> new TaskOptionDTO(option.isCorrect(), option.getAlternative())).toList()
        );
    }
}
