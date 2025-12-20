package br.com.alura.AluraFake.services;

import br.com.alura.AluraFake.domain.Course;
import br.com.alura.AluraFake.domain.User;
import br.com.alura.AluraFake.domain.enums.CourseStatus;
import br.com.alura.AluraFake.domain.enums.TaskType;
import br.com.alura.AluraFake.dto.course.InstructorCourseReportDTO;
import br.com.alura.AluraFake.exception.DataIntegrityException;
import br.com.alura.AluraFake.exception.ObjectNotFoundException;
import br.com.alura.AluraFake.mappers.CourseMapper;
import br.com.alura.AluraFake.repositories.CourseRepository;
import br.com.alura.AluraFake.repositories.TaskRepository;
import br.com.alura.AluraFake.repositories.UserRepository;
import br.com.alura.AluraFake.util.FormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseMapper courseMapper;

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

    public List<InstructorCourseReportDTO> findByInstructor(Long instructorId){
        User user = userRepository.findById(instructorId).orElseThrow(() -> new ObjectNotFoundException(User.class));
        if (!user.isInstructor())
            throw new IllegalArgumentException("Não foi possível entregar o relatório, pois o usuário não é um Instrutor");

        return courseRepository.listCurseReportByInstructor(instructorId).stream()
                .map(c -> courseMapper.toInstructorCourseReportFromInterface(c)).toList();

    }
}
