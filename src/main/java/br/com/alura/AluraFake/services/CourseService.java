package br.com.alura.AluraFake.services;

import br.com.alura.AluraFake.domain.Course;
import br.com.alura.AluraFake.domain.enums.CourseStatus;
import br.com.alura.AluraFake.domain.enums.TaskType;
import br.com.alura.AluraFake.exception.DataIntegrityException;
import br.com.alura.AluraFake.exception.ObjectNotFoundException;
import br.com.alura.AluraFake.repositories.CourseRepository;
import br.com.alura.AluraFake.repositories.TaskRepository;
import br.com.alura.AluraFake.util.FormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TaskRepository taskRepository;

    public Course findById(Long id){
        return courseRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(Course.class));
    }

    public Course publish(Long id){
        Course course = findById(id);
        if (!course.getStatus().equals(CourseStatus.BUILDING))
            throw new DataIntegrityException("Não é possível publicar um curso que não está com o status: Building");

        if (!taskRepository.verifyTypeOfTasksByCourseId(id, TaskType.values().length, FormatUtil.getAllCodesFromEnum(TaskType.class)))
            throw new DataIntegrityException("Não é possível publicar um curso que não possua todos os tipos de tarefas! ");

        course.setStatus(CourseStatus.PUBLISHED);
        course.setPublishedAt(LocalDateTime.now());
        return courseRepository.save(course);
    }
}
