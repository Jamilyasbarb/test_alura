package br.com.alura.AluraFake.domain;

import br.com.alura.AluraFake.domain.enums.TaskType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

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
    @OneToMany(mappedBy = "task", cascade = CascadeType.PERSIST)
    private List<TaskOption> taskOptions;
    private Integer type;


    public Task() {
    }

    public Task(Long id, String statement, Integer order, Course course, TaskType type) {
        this.id = id;
        this.statement = statement;
        this.order = order;
        this.course = course;
        this.type = type.getCode();
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

    public List<TaskOption> getTaskOptions() {
        return taskOptions;
    }

    public void setTaskOptions(List<TaskOption> taskOptions) {
        this.taskOptions = taskOptions;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
