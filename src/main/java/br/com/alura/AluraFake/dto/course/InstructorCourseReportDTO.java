package br.com.alura.AluraFake.dto.course;

import java.util.List;

public record InstructorCourseReportDTO(
        List<CourseListDTO> courses,
        Integer numberOfCoursesPublished
) {
}
