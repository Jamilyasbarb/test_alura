package br.com.alura.AluraFake.domain;

import br.com.alura.AluraFake.dto.TaskOptionDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class TaskOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "option_text")
    private String alternative;
    private boolean isCorrect;
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public TaskOption() {
    }

    public TaskOption(Long id, String alternative, boolean isCorrect) {
        this.id = id;
        this.alternative = alternative;
        this.isCorrect = isCorrect;
    }

    public TaskOption(TaskOptionDTO taskOptionDTO) {
        this.alternative = taskOptionDTO.option();
        this.isCorrect = taskOptionDTO.isCorrect();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlternative() {
        return alternative;
    }

    public void setAlternative(String alternative) {
        this.alternative = alternative;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
