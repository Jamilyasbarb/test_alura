package br.com.alura.AluraFake.services;

import br.com.alura.AluraFake.domain.Course;
import br.com.alura.AluraFake.domain.Task;
import br.com.alura.AluraFake.domain.TaskOption;
import br.com.alura.AluraFake.domain.enums.CourseStatus;
import br.com.alura.AluraFake.domain.enums.TaskType;
import br.com.alura.AluraFake.dto.course.CourseDTO;
import br.com.alura.AluraFake.dto.task.TaskDTO;
import br.com.alura.AluraFake.dto.task.TaskOptionDTO;
import br.com.alura.AluraFake.dto.task.CreateTaskDTO;
import br.com.alura.AluraFake.dto.user.UserDTO;
import br.com.alura.AluraFake.exception.DataIntegrityException;
import br.com.alura.AluraFake.exception.ObjectNotFoundException;
import br.com.alura.AluraFake.mappers.TaskMapper;
import br.com.alura.AluraFake.repositories.CourseRepository;
import br.com.alura.AluraFake.repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveTask_whenEverythingIsValid() {
        Course course = new Course();
        course.setStatus(CourseStatus.BUILDING);

        CreateTaskDTO dto = new CreateTaskDTO(1L, "Enunciado", 1, List.of());

        Task mappedTask = new Task();
        mappedTask.setStatement("Enunciado");
        TaskDTO taskDTO = new TaskDTO(mappedTask.getId(), mappedTask.getStatement(), mappedTask.getOrder(),
                new CourseDTO(course.getId(), course.getTitle(), new UserDTO(1L, "Jamily")), List.of());

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.findTaskAlreadyUtilizedByCourseId("Enunciado", 1L)).thenReturn(List.of());
        when(taskMapper.toEntityFromCreateDTO(dto, course, TaskType.OPEN_TEXT)).thenReturn(mappedTask);
        when(taskRepository.save(mappedTask)).thenReturn(mappedTask);
        when(taskMapper.toTaskDTOFromEntity(mappedTask)).thenReturn(taskDTO);

        TaskDTO savedTask = taskService.createTask(dto);


        assertEquals("Enunciado", savedTask.statement());
        verify(taskRepository, times(1)).save(mappedTask);
    }


    @Test
    void shouldPass_whenAllRulesSatisfied_singleChoice(){
        Course course = new Course();
        course.setStatus(CourseStatus.BUILDING);

        TaskOptionDTO taskOptionDTO1 = new TaskOptionDTO(true, "Alternativa 1");
        TaskOptionDTO taskOptionDTO2 = new TaskOptionDTO(false, "Alternativa 2");
        TaskOptionDTO taskOptionDTO3 = new TaskOptionDTO(false, "Alternativa 3");

        CreateTaskDTO createTaskDTO =
                new CreateTaskDTO(1L, "Enunciado", 1, List.of(taskOptionDTO1, taskOptionDTO2, taskOptionDTO3));


        TaskOption taskOption1 = new TaskOption(1L,"Alternativa 1", true);
        TaskOption taskOption2 = new TaskOption( 2L,"Alternativa 2", false);
        TaskOption taskOption3 = new TaskOption(3L, "Alternativa 3", false );

        Task mappedTask = new Task();
        mappedTask.setStatement("Enunciado");
        mappedTask.setTaskOptions(List.of(taskOption1, taskOption2, taskOption3));
        TaskDTO taskDTO = new TaskDTO(mappedTask.getId(), mappedTask.getStatement(), mappedTask.getOrder(),
                new CourseDTO(course.getId(), course.getTitle(), new UserDTO(1L, "Jamily")), List.of(taskOptionDTO1, taskOptionDTO2, taskOptionDTO3));



        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.findTaskAlreadyUtilizedByCourseId("Enunciado", 1L)).thenReturn(List.of());
        when(taskMapper.toEntityFromCreateDTO(createTaskDTO, course, TaskType.SINGLE_CHOICE)).thenReturn(mappedTask);
        when(taskRepository.save(mappedTask)).thenReturn(mappedTask);
        when(taskMapper.toTaskDTOFromEntity(mappedTask)).thenReturn(taskDTO);

        TaskDTO savedTask = taskService.createTaskOneChoice(createTaskDTO, false);

        assertEquals("Enunciado", savedTask.statement());
        verify(taskRepository, times(1)).save(mappedTask);
    }

    @Test
    void shouldPass_whenAllRulesSatisfied_multipleChoice(){
        Course course = new Course();
        course.setStatus(CourseStatus.BUILDING);

        TaskOptionDTO taskOptionDTO1 = new TaskOptionDTO(true, "Alternativa 1");
        TaskOptionDTO taskOptionDTO2 = new TaskOptionDTO(true, "Alternativa 2");
        TaskOptionDTO taskOptionDTO3 = new TaskOptionDTO(false, "Alternativa 3");

        CreateTaskDTO createTaskDTO =
                new CreateTaskDTO(1L, "Enunciado", 1, List.of(taskOptionDTO1, taskOptionDTO2, taskOptionDTO3));


        TaskOption taskOption1 = new TaskOption(1L,"Alternativa 1", true);
        TaskOption taskOption2 = new TaskOption( 2L,"Alternativa 2", true);
        TaskOption taskOption3 = new TaskOption(3L, "Alternativa 3", false );

        Task mappedTask = new Task();
        mappedTask.setStatement("Enunciado");
        mappedTask.setTaskOptions(List.of(taskOption1, taskOption2, taskOption3));

        TaskDTO taskDTO = new TaskDTO(mappedTask.getId(), mappedTask.getStatement(), mappedTask.getOrder(),
                new CourseDTO(course.getId(), course.getTitle(), new UserDTO(1L, "Jamily")), List.of(taskOptionDTO1, taskOptionDTO2, taskOptionDTO3));



        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.findTaskAlreadyUtilizedByCourseId("Enunciado", 1L)).thenReturn(List.of());
        when(taskMapper.toEntityFromCreateDTO(createTaskDTO, course, TaskType.MULTIPLE_CHOICE)).thenReturn(mappedTask);
        when(taskRepository.save(mappedTask)).thenReturn(mappedTask);
        when(taskMapper.toTaskDTOFromEntity(mappedTask)).thenReturn(taskDTO);

        TaskDTO savedTask = taskService.createTaskOneChoice(createTaskDTO, true);

        assertEquals("Enunciado", savedTask.statement());
        verify(taskRepository, times(1)).save(mappedTask);
    }

    @Test
    void shouldThrowException_whenTheFirstTaskAddWithOrderDifferentThenOne(){
        when(taskRepository.findByOrder(1)).thenReturn(Optional.empty());
        when(taskRepository.existsAnyTask()).thenReturn(false);

        DataIntegrityException exception = assertThrows(DataIntegrityException.class, () -> taskService.verifyAndChangeOrder(2));
        assertEquals("A ordem deve começar do número 1!", exception.getMessage());
    }

    @Test
    void shouldThrowException_whenTheAddedOrderNumberDoesNotContinueTheSequenceFromThePreviousOne(){
        when(taskRepository.findByOrder(1)).thenReturn(Optional.empty());
        when(taskRepository.existsAnyTask()).thenReturn(true);
        when(taskRepository.findLastTaskId()).thenReturn(1L);

        DataIntegrityException exception = assertThrows(DataIntegrityException.class, () -> taskService.verifyAndChangeOrder(3));
        assertEquals("Não é permitido adicionar uma atividade com ordem 3 pois ainda não existem atividades com ordens 2!", exception.getMessage());
    }


    @Test
    void shouldThrowException_whenCourseNotFound() {
        CreateTaskDTO dto = new CreateTaskDTO(1L, "Enunciado", 1, List.of());

        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> taskService.createTask(dto));
    }

    @Test
    void shouldThrowException_whenCourseStatusNotBuilding() {
        Course course = new Course();
        course.setStatus(CourseStatus.PUBLISHED);

        CreateTaskDTO dto = new CreateTaskDTO(1L, "Enunciado", 1, List.of());

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        DataIntegrityException exception = assertThrows(DataIntegrityException.class, () -> taskService.createTask(dto));
        assertEquals("Não foi possível adicionar a tarefa pois o status do Curso não está BUILDING", exception.getMessage());
    }

    @Test
    void shouldThrowException_whenTaskAlreadyExists() {
        Course course = new Course();
        course.setStatus(CourseStatus.BUILDING);

        CreateTaskDTO dto = new CreateTaskDTO(1L, "Enunciado", 1, List.of());

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.findTaskAlreadyUtilizedByCourseId("Enunciado", 1L))
                .thenReturn(List.of(new Task()));

        DataIntegrityException exception = assertThrows(DataIntegrityException.class, () -> taskService.createTask(dto));
        assertEquals("Já existe uma Tarefa com esse enunciado!", exception.getMessage());
    }

    @Test
    void shouldThrowException_whenTheNumberOfAlternativesEnteredIsGreaterThanExpected() {
        TaskOptionDTO taskOptionDTO1 = new TaskOptionDTO(true, "Alternativa 1");
        TaskOptionDTO taskOptionDTO2 = new TaskOptionDTO(true, "Alternativa 2");
        TaskOptionDTO taskOptionDTO3 = new TaskOptionDTO(true, "Alternativa 3");
        TaskOptionDTO taskOptionDTO4 = new TaskOptionDTO(true, "Alternativa 4");
        TaskOptionDTO taskOptionDTO5 = new TaskOptionDTO(true, "Alternativa 5");
        TaskOptionDTO taskOptionDTO6 = new TaskOptionDTO(true, "Alternativa 6");
        CreateTaskDTO taskDTO = new CreateTaskDTO(1L, "Enunciado", 1, Arrays.asList(taskOptionDTO1, taskOptionDTO2,
                taskOptionDTO3, taskOptionDTO4, taskOptionDTO5, taskOptionDTO6));

        DataIntegrityException exception = assertThrows(DataIntegrityException.class, () -> taskService.validateTaskOneChoice(taskDTO, false));
        assertEquals("Adicione de 2 a 5 alternativas.", exception.getMessage());

    }

    @Test
    void shouldThrowException_whenTheNumberOfAlternativesEntered_IsLessThanExpected() {
        TaskOptionDTO taskOptionDTO1 = new TaskOptionDTO(true, "Alternativa 1");

        CreateTaskDTO taskDTO = new CreateTaskDTO(1L, "Enunciado", 1, List.of(taskOptionDTO1));

        DataIntegrityException exception = assertThrows(DataIntegrityException.class, () -> taskService.validateTaskOneChoice(taskDTO, false));
        assertEquals("Adicione de 2 a 5 alternativas.", exception.getMessage());

    }

    @Test
    void shouldThrowException_whenMultipleChoice_lessThanTwoCorrectOptions(){
        TaskOptionDTO taskOptionDTO1 = new TaskOptionDTO(true, "Alternativa 1");
        TaskOptionDTO taskOptionDTO2 = new TaskOptionDTO(false, "Alternativa 2");
        TaskOptionDTO taskOptionDTO3 = new TaskOptionDTO(false, "Alternativa 3");
        List<TaskOptionDTO> taskOptionDTOS = List.of(taskOptionDTO1, taskOptionDTO2, taskOptionDTO3);

        CreateTaskDTO taskDTO = new CreateTaskDTO(1L, "Enunciado", 1, taskOptionDTOS);

        DataIntegrityException exception = assertThrows(DataIntegrityException.class, () -> taskService.validateTaskOneChoice(taskDTO, true));
        assertEquals("Assine duas ou mais alternativas", exception.getMessage());
    }

    @Test
    void shouldThrowException_whenMultipleChoice_noIncorrectOption(){
        TaskOptionDTO taskOptionDTO1 = new TaskOptionDTO(true, "Alternativa 1");
        TaskOptionDTO taskOptionDTO2 = new TaskOptionDTO(true, "Alternativa 2");
        TaskOptionDTO taskOptionDTO3 = new TaskOptionDTO(true, "Alternativa 3");
        List<TaskOptionDTO> taskOptionDTOS = List.of(taskOptionDTO1, taskOptionDTO2, taskOptionDTO3);

        CreateTaskDTO taskDTO = new CreateTaskDTO(1L, "Enunciado", 1, taskOptionDTOS);

        DataIntegrityException exception = assertThrows(DataIntegrityException.class, () -> taskService.validateTaskOneChoice(taskDTO, true));
        assertEquals("Pelo menos uma alternativa precisa ser Incorreta! ", exception.getMessage());
    }

    @Test
    void shouldThrowException_whenSingleChoice_moreThanOneCorrectOption(){
        TaskOptionDTO taskOptionDTO1 = new TaskOptionDTO(true, "Alternativa 1");
        TaskOptionDTO taskOptionDTO2 = new TaskOptionDTO(true, "Alternativa 2");
        TaskOptionDTO taskOptionDTO3 = new TaskOptionDTO(false, "Alternativa 3");
        List<TaskOptionDTO> taskOptionDTOS = List.of(taskOptionDTO1, taskOptionDTO2, taskOptionDTO3);

        CreateTaskDTO taskDTO = new CreateTaskDTO(1L, "Enunciado", 1, taskOptionDTOS);

        DataIntegrityException exception = assertThrows(DataIntegrityException.class, () -> taskService.validateTaskOneChoice(taskDTO, false));
        assertEquals("Assine apenas uma opção como correta.", exception.getMessage());
    }


}