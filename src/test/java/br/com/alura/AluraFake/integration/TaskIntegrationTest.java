package br.com.alura.AluraFake.integration;

import br.com.alura.AluraFake.domain.Course;
import br.com.alura.AluraFake.domain.Task;
import br.com.alura.AluraFake.domain.User;
import br.com.alura.AluraFake.domain.enums.CourseStatus;
import br.com.alura.AluraFake.domain.enums.Role;
import br.com.alura.AluraFake.dto.task.TaskOptionDTO;
import br.com.alura.AluraFake.dto.task.CreateTaskDTO;
import br.com.alura.AluraFake.mappers.TaskMapper;
import br.com.alura.AluraFake.repositories.CourseRepository;
import br.com.alura.AluraFake.repositories.TaskRepository;
import br.com.alura.AluraFake.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskMapper taskMapper;

    private Course course;

    @BeforeEach
    void setup() {
        User jamily = new User("jamily", "jamily@alura.com.br", Role.STUDENT);
        User felipe = new User("felipe", "felipe@alura.com.br", Role.INSTRUCTOR);
        userRepository.saveAll(Arrays.asList(jamily, felipe));
        course = new Course();
        course.setDescription("Aprenda Java com Alura23");
        course.setTitle("Curso de Teste");
        course.setStatus(CourseStatus.BUILDING);
        course.setInstructor(felipe);
        courseRepository.save(course);
    }

    //openText

    @Test
    void shouldCreateOpenTextTaskSuccessfully() throws Exception {
        CreateTaskDTO dto = new CreateTaskDTO(
                course.getId(),
                "Enunciado da tarefa",
                1,
                null
        );

        String dtoJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/task/new/opentext")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.statement").value("Enunciado da tarefa"))
                .andExpect(jsonPath("$.order").value(1));

        List<Task> tasks = taskRepository.findAll();
        assertEquals(1, tasks.size());
        assertEquals("Enunciado da tarefa", tasks.get(0).getStatement());
    }

    @Test
    void shouldFailWhenCourseNotBuilding() throws Exception {
        course.setStatus(CourseStatus.PUBLISHED);
        courseRepository.save(course);

        CreateTaskDTO dto = new CreateTaskDTO(course.getId(), "Enunciado", 1, List.of());
        String dtoJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/task/new/opentext")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoJson))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Não foi possível adicionar a tarefa pois o status do Curso não está BUILDING"));
    }

    @Test
    void shouldFailWhenTaskAlreadyExists() throws Exception {
        Task existing = new Task();
        existing.setStatement("Enunciado duplicado");
        existing.setOrder(1);
        existing.setCourse(course);
        taskRepository.save(existing);

        CreateTaskDTO dto = new CreateTaskDTO(course.getId(), "Enunciado duplicado", 2, List.of());
        String dtoJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/task/new/opentext")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoJson))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Já existe uma Tarefa com esse enunciado!"));
    }

    @Test
    void shouldFailWhenOrderIsIncorrect() throws Exception {
        CreateTaskDTO dto = new CreateTaskDTO(course.getId(), "Nova Tarefa", 4, List.of());
        String dtoJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/task/new/opentext")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoJson))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("A ordem deve começar do número 1!"));
    }

    //singleChoice
    @Test
    void shouldCreateSingleChoiceTaskSuccessfully() throws Exception {
        CreateTaskDTO dto = new CreateTaskDTO(
                course.getId(),
                "Enunciado da tarefa",
                1,
                List.of(
                        new TaskOptionDTO(true, "Opção 1"),
                        new TaskOptionDTO(false, "Opção 2")
                )
        );

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/task/new/singlechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.statement").value("Enunciado da tarefa"))
                .andExpect(jsonPath("$.taskOptionDTOS").isArray())
                .andExpect(jsonPath("$.taskOptionDTOS[0].option").value("Opção 1"))
                .andExpect(jsonPath("$.taskOptionDTOS[1].option").value("Opção 2"));

        List<Task> tasks = taskRepository.findAll();
        assertEquals(1, tasks.size());
        assertEquals(2, tasks.get(0).getTaskOptions().size());
    }

    @Test
    void shouldFail_whenMultipleCorrectOptions() throws Exception {
        CreateTaskDTO dto = new CreateTaskDTO(
                course.getId(),
                "Enunciado da tarefa",
                1,
                List.of(
                        new TaskOptionDTO(true, "Opção 1"),
                        new TaskOptionDTO(true, "Opção 2")
                )
        );

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/task/new/singlechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError());
    }


    @Test
    void shouldFail_whenOptionDuplicated() throws Exception {
        CreateTaskDTO dto = new CreateTaskDTO(
                course.getId(),
                "Enunciado da tarefa",
                1,
                List.of(
                        new TaskOptionDTO(true, "Opção 1"),
                        new TaskOptionDTO(false,"Opção 1")
                )
        );

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/task/new/singlechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldFail_whenOptionEqualsStatement() throws Exception {
        CreateTaskDTO dto = new CreateTaskDTO(
                course.getId(),
                "Opção 1",
                1,
                List.of(
                        new TaskOptionDTO(true, "Opção 1"),
                        new TaskOptionDTO(false, "Opção 2")
                )
        );

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/task/new/singlechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError());
    }

    //multipleChoice
    @Test
    void shouldCreateMultipleChoiceTaskSuccessfully() throws Exception {
        CreateTaskDTO dto = new CreateTaskDTO(
                course.getId(),
                "Enunciado da tarefa",
                1,
                List.of(
                        new TaskOptionDTO(true,"Opção 1"),
                        new TaskOptionDTO(true, "Opção 2"),
                        new TaskOptionDTO(false,"Opção 3")
                )
        );

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/task/new/multiplechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.statement").value("Enunciado da tarefa"))
                .andExpect(jsonPath("$.taskOptionDTOS").isArray())
                .andExpect(jsonPath("$.taskOptionDTOS.length()").value(3));

        // Verifica no banco
        List<Task> tasks = taskRepository.findAll();
        assertEquals(1, tasks.size());
        assertEquals(3, tasks.get(0).getTaskOptions().size());
    }

    @Test
    void shouldFail_whenLessThanThreeOptions() throws Exception {
        CreateTaskDTO dto = new CreateTaskDTO(
                course.getId(),
                "Enunciado da tarefa",
                1,
                List.of(
                        new TaskOptionDTO(true, "Opção 1"),
                        new TaskOptionDTO(false,"Opção 2")
                )
        );

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/task/new/multiplechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldFail_whenOnlyOneCorrectOption() throws Exception {
        CreateTaskDTO dto = new CreateTaskDTO(
                course.getId(),
                "Enunciado da tarefa",
                1,
                List.of(
                        new TaskOptionDTO(true, "Opção 1"),
                        new TaskOptionDTO(false, "Opção 2"),
                        new TaskOptionDTO(false, "Opção 3")
                )
        );

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/task/new/multiplechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldFail_whenAllOptionsAreCorrect() throws Exception {
        CreateTaskDTO dto = new CreateTaskDTO(
                course.getId(),
                "Enunciado da tarefa",
                1,
                List.of(
                        new TaskOptionDTO(true, "Opção 1"),
                        new TaskOptionDTO(true,"Opção 2"),
                        new TaskOptionDTO(true,"Opção 3")
                )
        );

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/task/new/multiplechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError());
    }

}
