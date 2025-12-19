package br.com.alura.AluraFake.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.List;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String statement;
    @Column(name = "order_number")
    private Integer order;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    //    @OneToMany(mappedBy = "task")
//    private List<TaskOptions> taskOptions;


    public Task() {
    }

    public Task(Long id, String statement, Integer order, List<TaskOptions> taskOptions, Course course) {
        this.id = id;
        this.statement = statement;
        this.order = order;
//        this.taskOptions = taskOptions;
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

//    public List<TaskOptions> getTaskOptions() {
//        return taskOptions;
//    }
//
//    public void setTaskOptions(List<TaskOptions> taskOptions) {
//        this.taskOptions = taskOptions;
//    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
