package br.com.alura.AluraFake.dto.course;

public record InstructorCourseReportDTO(
        Long id,
        String title,
        String status,
        String publishedAt,
        Integer numberOfQuestion,
        Integer numberOfCoursesPublished
) {
}
