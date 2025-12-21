package br.com.alura.AluraFake.controllers;

import br.com.alura.AluraFake.domain.Course;
import br.com.alura.AluraFake.domain.Task;
import br.com.alura.AluraFake.domain.TaskOption;
import br.com.alura.AluraFake.domain.enums.TaskType;
import br.com.alura.AluraFake.dto.course.CourseDTO;
import br.com.alura.AluraFake.dto.task.TaskDTO;
import br.com.alura.AluraFake.dto.task.TaskOptionDTO;
import br.com.alura.AluraFake.dto.task.CreateTaskDTO;
import br.com.alura.AluraFake.dto.user.UserDTO;
import br.com.alura.AluraFake.exception.DataIntegrityException;
import br.com.alura.AluraFake.exception.ObjectNotFoundException;
import br.com.alura.AluraFake.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void shouldPass_whenAllIsRightOpenText() throws Exception {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO(3L, "Enunciado", 1, List.of());
        Task task = new Task(1L, "Enunciado", 1, new Course(), TaskType.OPEN_TEXT);

        doReturn(task).when(taskService).createTask(any(CreateTaskDTO.class));

        mockMvc.perform(post("/task/new/opentext")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDTO)))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    void shouldPass_whenAllIsRightSingleChoice() throws Exception {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO(3L, "Enunciado", 1, List.of());
        Task task = new Task(1L, "Enunciado", 1, new Course(), TaskType.OPEN_TEXT);

        TaskOption taskOption1 = new TaskOption(1L, "Alternativa 1", false);
        TaskOption taskOption2 = new TaskOption(1L, "Alternativa 2", true);
        TaskDTO taskDTO = new TaskDTO(task.getId(), task.getStatement(), task.getOrder(),
                new CourseDTO(1L, "Curso1", new UserDTO(1L, "Jamily")), List.of());

        task.setTaskOptions(List.of(taskOption1, taskOption2));

        doReturn(taskDTO).when(taskService).createTaskOneChoice(any(CreateTaskDTO.class), anyBoolean());

        mockMvc.perform(post("/task/new/singlechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDTO)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void shouldPass_whenAllIsRightMultipleChoice() throws Exception {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO(3L, "Enunciado", 1, List.of());
        Task task = new Task(1L, "Enunciado", 1, new Course(), TaskType.OPEN_TEXT);

        TaskOption taskOption1 = new TaskOption(1L, "Alternativa 1", false);
        TaskOption taskOption2 = new TaskOption(1L, "Alternativa 2", true);
        TaskDTO taskDTO = new TaskDTO(task.getId(), task.getStatement(), task.getOrder(),
                new CourseDTO(1L, "Curso1", new UserDTO(1L, "Jamily")), List.of());

        task.setTaskOptions(List.of(taskOption1, taskOption2));

        doReturn(taskDTO).when(taskService).createTaskOneChoice(any(CreateTaskDTO.class), anyBoolean());

        mockMvc.perform(post("/task/new/multiplechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDTO)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void shouldThrowException_whenObjectNotFoundOpenText() throws Exception {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO(3L, "Enunciado", 1, List.of());

        doThrow(new ObjectNotFoundException(Course.class))
                .when(taskService).createTask(any(CreateTaskDTO.class));

        mockMvc.perform(post("/task/new/opentext")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").isNotEmpty());

    }

    @Test
    void shouldThrowException_whenDataIntegrityExceptionOpenText() throws Exception {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO(3L, "Enunciado", 1, List.of());

        doThrow(new DataIntegrityException("Any"))
                .when(taskService).createTask(any(CreateTaskDTO.class));

        mockMvc.perform(post("/task/new/opentext")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").isNotEmpty());

    }

    @Test
    void shouldThrowException_whenOpenTextDTOIsInvalid() throws Exception {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO(3L, "E", 1, List.of());

        mockMvc.perform(post("/task/new/opentext")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").isNotEmpty());

    }

    @Test
    void shouldThrowException_whenObjectNotFoundSingleChoice() throws Exception {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO(3L, "Enunciado", 1, List.of());

        doThrow(new ObjectNotFoundException(Course.class))
                .when(taskService).createTaskOneChoice(any(CreateTaskDTO.class), anyBoolean());

        mockMvc.perform(post("/task/new/singlechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").isNotEmpty());

    }

    @Test
    void shouldThrowException_whenDataIntegrityExceptionSingleChoice() throws Exception {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO(3L, "Enunciado", 1, List.of());

        doThrow(new DataIntegrityException("Any"))
                .when(taskService).createTaskOneChoice(any(CreateTaskDTO.class), anyBoolean());

        mockMvc.perform(post("/task/new/singlechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").isNotEmpty());

    }

    @Test
    void shouldThrowException_whenSingleChoiceDTOIsInvalid() throws Exception {
        TaskOptionDTO taskOption1 = new TaskOptionDTO(false, "O");
        TaskOptionDTO taskOption2 = new TaskOptionDTO(true, "O");
        CreateTaskDTO createTaskDTO = new CreateTaskDTO(3L, "Enunciado", 1, List.of(taskOption1, taskOption2));

        mockMvc.perform(post("/task/new/singlechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").isNotEmpty());

    }

    @Test
    void shouldThrowException_whenObjectNotFoundMultipleChoice() throws Exception {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO(3L, "Enunciado", 1, List.of());

        doThrow(new ObjectNotFoundException(Course.class))
                .when(taskService).createTaskOneChoice(any(CreateTaskDTO.class), anyBoolean());

        mockMvc.perform(post("/task/new/multiplechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").isNotEmpty());

    }

    @Test
    void shouldThrowException_whenDataIntegrityExceptionMultipleChoice() throws Exception {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO(3L, "Enunciado", 1, List.of());

        doThrow(new DataIntegrityException("Any"))
                .when(taskService).createTaskOneChoice(any(CreateTaskDTO.class), anyBoolean());

        mockMvc.perform(post("/task/new/multiplechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").isNotEmpty());

    }

    @Test
    void shouldThrowException_whenMultipleChoiceDTOIsInvalid() throws Exception {
        TaskOptionDTO taskOption1 = new TaskOptionDTO(false, "O");
        TaskOptionDTO taskOption2 = new TaskOptionDTO(true, "O");
        TaskOptionDTO taskOption3 = new TaskOptionDTO(true, "O");

        CreateTaskDTO createTaskDTO = new CreateTaskDTO(3L, "Enunciado", 1, List.of(taskOption1, taskOption2, taskOption3));



        mockMvc.perform(post("/task/new/multiplechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").isNotEmpty());

    }


    @Test
    void newSingleChoice() {
    }

    @Test
    void newMultipleChoice() {
    }
}