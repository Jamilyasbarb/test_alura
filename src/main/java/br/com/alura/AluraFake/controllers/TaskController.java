package br.com.alura.AluraFake.controllers;

import br.com.alura.AluraFake.dto.task.CreateTaskDTO;
import br.com.alura.AluraFake.dto.task.TaskDTO;
import br.com.alura.AluraFake.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/new/opentext")
    public ResponseEntity<TaskDTO> newOpenTextExercise(@Valid @RequestBody CreateTaskDTO createTaskDTO) {
        TaskDTO taskDTO = taskService.createTask(createTaskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskDTO);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/new/singlechoice")
    public ResponseEntity<TaskDTO> newSingleChoice(@Valid @RequestBody CreateTaskDTO createTaskDTO) {
        TaskDTO task = taskService.createTaskOneChoice(createTaskDTO, false);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/new/multiplechoice")
    public ResponseEntity newMultipleChoice(@Valid @RequestBody CreateTaskDTO createTaskDTO) {
        TaskDTO task = taskService.createTaskOneChoice(createTaskDTO, true);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

}