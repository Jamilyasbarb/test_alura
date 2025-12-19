package br.com.alura.AluraFake.controllers;

import br.com.alura.AluraFake.domain.Task;
import br.com.alura.AluraFake.dto.task.CreateTaskDTO;
import br.com.alura.AluraFake.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/new/opentext")
    public ResponseEntity newOpenTextExercise(@Valid @RequestBody CreateTaskDTO createTaskDTO) {
        Task task = taskService.createTask(createTaskDTO);
        return ResponseEntity.ok().body(task);
    }

    @PostMapping("/new/singlechoice")
    public ResponseEntity newSingleChoice(@Valid @RequestBody CreateTaskDTO createTaskDTO) {
        Task task = taskService.createTaskOneChoice(createTaskDTO);
        return ResponseEntity.ok().body(task);
    }

    @PostMapping("/new/multiplechoice")
    public ResponseEntity newMultipleChoice(@Valid @RequestBody CreateTaskDTO createTaskDTO) {
        Task task = taskService.createTaskOneChoice(createTaskDTO);
        return ResponseEntity.ok().body(task);
    }

}