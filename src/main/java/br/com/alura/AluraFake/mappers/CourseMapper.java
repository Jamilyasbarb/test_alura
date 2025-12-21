package br.com.alura.AluraFake.mappers;

import br.com.alura.AluraFake.domain.Course;
import br.com.alura.AluraFake.dto.course.CourseDTO;
import br.com.alura.AluraFake.dto.course.InstructorCourseReportDTO;
import br.com.alura.AluraFake.dto.course.InstructorCourseReportDTOInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    @Autowired
    private UserMapper userMapper;

    public InstructorCourseReportDTO toInstructorCourseReportFromInterface(InstructorCourseReportDTOInterface course){
        return new InstructorCourseReportDTO(
                course.getId(),
                course.getTitle(),
                course.getStatus(),
                course.getPublishedAt(),
                course.getNumberOfQuestion(),
                course.getNumberOfCoursesPublished()
        );
    }

    public CourseDTO toCourseDTOFromEntity(Course course){
        return new CourseDTO(
                course.getId(),
                course.getTitle(),
                userMapper.toUserDtoFromEntity(course.getInstructor())
        );
    }
}
