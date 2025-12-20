package br.com.alura.AluraFake.mappers;

import br.com.alura.AluraFake.dto.course.InstructorCourseReportDTO;
import br.com.alura.AluraFake.dto.course.InstructorCourseReportDTOInterface;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

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
}
