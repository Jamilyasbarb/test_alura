package br.com.alura.AluraFake.dto.course;

public interface InstructorCourseReportDTOInterface {
    Long getId();
    String getTitle();
    String getStatus();
    String getPublishedAt();
    Integer getNumberOfQuestion();
    Integer getNumberOfCoursesPublished();
}
