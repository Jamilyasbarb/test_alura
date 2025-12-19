package br.com.alura.AluraFake.services;

import br.com.alura.AluraFake.domain.Course;
import br.com.alura.AluraFake.domain.Task;
import br.com.alura.AluraFake.domain.enums.CourseStatus;
import br.com.alura.AluraFake.dto.task.CreateTaskDTO;
import br.com.alura.AluraFake.exception.DataIntegrityException;
import br.com.alura.AluraFake.exception.ObjectNotFoundException;
import br.com.alura.AluraFake.mappers.TaskMapper;
import br.com.alura.AluraFake.repositories.CourseRepository;
import br.com.alura.AluraFake.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TaskMapper taskMapper;

    public Task createTask(CreateTaskDTO createTaskDTO){
        Course course = courseRepository.findById(createTaskDTO.courseId()).orElseThrow(() -> new ObjectNotFoundException(Course.class));
        validateDefault(createTaskDTO, course);
        return taskRepository.save(taskMapper.toEntityFromCreateDTO(createTaskDTO, course));
    }

    public void validateDefault(CreateTaskDTO createTaskDTO, Course course){
        if (!course.getStatus().equals(CourseStatus.BUILDING))
            throw new DataIntegrityException("Não foi possível adicionar a tarefa pois o status do Curso não está BUILDING");


        List<Task> tasks = taskRepository.findTaskAlreadyUtilizedByCourseId(createTaskDTO.statement(), createTaskDTO.courseId());
        if (!tasks.isEmpty()){
            throw new DataIntegrityException("Já existe uma Tarefa com esse enunciado!");
        }
    }


}
