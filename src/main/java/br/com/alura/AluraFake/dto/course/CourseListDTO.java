package br.com.alura.AluraFake.dto.course;

public record CourseListDTO(
        Long id,
        String title,
        String status,
        String publishedAt,
        Integer numberOfQuestions
    ) {
}
