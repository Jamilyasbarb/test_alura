package br.com.alura.AluraFake.mappers;

import br.com.alura.AluraFake.domain.Course;
import br.com.alura.AluraFake.domain.Task;
import br.com.alura.AluraFake.dto.task.CreateTaskDTO;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntityFromCreateDTO(CreateTaskDTO createTaskDTO, Course course){
        return new Task(
                null,
                createTaskDTO.statement(),
                createTaskDTO.order(),
                course
        );
    }
}
