package br.com.alura.AluraFake.dto;

import br.com.alura.AluraFake.domain.enums.CourseStatus;
import br.com.alura.AluraFake.domain.Course;

import java.io.Serializable;

public class CourseListItemDTO implements Serializable {

    private Long id;
    private String title;
    private String description;
    private CourseStatus courseStatus;

    public CourseListItemDTO(Course course) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.description = course.getDescription();
        this.courseStatus = course.getStatus();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public CourseStatus getStatus() {
        return courseStatus;
    }
}
