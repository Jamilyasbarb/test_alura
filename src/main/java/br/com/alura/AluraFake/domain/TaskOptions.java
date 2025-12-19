package br.com.alura.AluraFake.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class TaskOptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String option;
    private boolean isCorrect;
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public TaskOptions() {
    }

    public TaskOptions(Long id, String option, boolean isCorrect) {
        this.id = id;
        this.option = option;
        this.isCorrect = isCorrect;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
