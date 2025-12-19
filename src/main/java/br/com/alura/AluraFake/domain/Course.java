package br.com.alura.AluraFake.domain;

import br.com.alura.AluraFake.domain.enums.CourseStatus;
import jakarta.persistence.*;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String title;
    private String description;
    @ManyToOne
    private User instructor;
    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus;
    private LocalDateTime publishedAt;

    @Deprecated
    public Course(){}

    public Course(String title, String description, User instructor) {
        Assert.isTrue(instructor.isInstructor(), "Usuario deve ser um instrutor");
        this.title = title;
        this.instructor = instructor;
        this.description = description;
        this.courseStatus = CourseStatus.BUILDING;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setStatus(CourseStatus courseStatus) {
        this.courseStatus = courseStatus;
    }

    public User getInstructor() {
        return instructor;
    }

    public String getDescription() {
        return description;
    }

    public CourseStatus getStatus() {
        return courseStatus;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }
}
