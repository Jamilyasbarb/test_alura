package br.com.alura.AluraFake.services;

import br.com.alura.AluraFake.domain.Course;
import br.com.alura.AluraFake.domain.Task;
import br.com.alura.AluraFake.domain.TaskOptions;
import br.com.alura.AluraFake.domain.enums.CourseStatus;
import br.com.alura.AluraFake.domain.enums.TaskType;
import br.com.alura.AluraFake.dto.TaskOptionDTO;
import br.com.alura.AluraFake.dto.task.CreateTaskDTO;
import br.com.alura.AluraFake.exception.DataIntegrityException;
import br.com.alura.AluraFake.exception.ObjectNotFoundException;
import br.com.alura.AluraFake.mappers.TaskMapper;
import br.com.alura.AluraFake.repositories.CourseRepository;
import br.com.alura.AluraFake.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TaskMapper taskMapper;

    public Task createTask(CreateTaskDTO createTaskDTO){
        Course course = courseRepository.findById(createTaskDTO.courseId()).orElseThrow(() -> new ObjectNotFoundException(Course.class));
        validateDefault(createTaskDTO, course);
        return taskRepository.save(taskMapper.toEntityFromCreateDTO(createTaskDTO, course, TaskType.OPEN_TEXT));
    }

    public Task createTaskOneChoice(CreateTaskDTO createTaskDTO, boolean isMultipleChoice){
        Course course = courseRepository.findById(createTaskDTO.courseId()).orElseThrow(() -> new ObjectNotFoundException(Course.class));
        validateDefault(createTaskDTO, course);
        validateTaskOneChoice(createTaskDTO, isMultipleChoice);
        Task task = taskMapper.toEntityFromCreateDTO(createTaskDTO, course, isMultipleChoice ? TaskType.MULTIPLE_CHOICE : TaskType.SINGLE_CHOICE);

        if (Objects.nonNull(createTaskDTO.options())) {
            List<TaskOptions> options = createTaskDTO.options().stream()
                    .map(TaskOptions::new)
                    .toList();

            options.forEach(option -> option.setTask(task));
            task.setTaskOptions(options);
        }
        return taskRepository.save(task);
    }

    public void validateDefault(CreateTaskDTO createTaskDTO, Course course){
        if (!course.getStatus().equals(CourseStatus.BUILDING))
            throw new DataIntegrityException("Não foi possível adicionar a tarefa pois o status do Curso não está BUILDING");

        //todo: remover acentos e letra maiusculas antes da comparacao
        List<Task> tasks = taskRepository.findTaskAlreadyUtilizedByCourseId(createTaskDTO.statement(), createTaskDTO.courseId());
        if (!tasks.isEmpty()){
            throw new DataIntegrityException("Já existe uma Tarefa com esse enunciado!");
        }
        verifyAndChangeOrder(createTaskDTO.order());
    }

    public void validateTaskOneChoice(CreateTaskDTO createTaskDTO, boolean isMultipleChoice){
        int minOption = isMultipleChoice ? 3 : 2; //todo: colocar em contants
        if (createTaskDTO.options().size() < minOption || createTaskDTO.options().size() > 5)
            throw new DataIntegrityException("Adicione de 2 a 5 alternativas.");

        List<TaskOptionDTO> optionsChoice = createTaskDTO.options().stream().filter(TaskOptionDTO::isCorrect).toList();


        if (isMultipleChoice){
            if (optionsChoice.size()<= 1)
                throw new DataIntegrityException("Assine duas ou mais alternativas");

            List<TaskOptionDTO> optionsNotChoice = createTaskDTO.options().stream().filter(t -> !t.isCorrect()).toList();
            if (optionsNotChoice.isEmpty())
                throw new DataIntegrityException("Pelo menos uma alternativa precisa ser Incorreta! ");

        }else{
            if (optionsChoice.size() > 1)
                throw new DataIntegrityException("Assine apenas uma opção como correta.");
        }

        verifyDuplicateOptions(createTaskDTO);
    }


    private void verifyDuplicateOptions(CreateTaskDTO createTaskDTO){
        for (int i = 0; i < createTaskDTO.options().size(); i++) {
            TaskOptionDTO taskOptionI = createTaskDTO.options().get(i);
            for (int j = i+1; j < createTaskDTO.options().size(); j++) {
                TaskOptionDTO taskOptionJ = createTaskDTO.options().get(j);
                if (taskOptionI.option().equalsIgnoreCase(taskOptionJ.option()) || taskOptionI.option().equalsIgnoreCase(createTaskDTO.statement()))
                    throw new DataIntegrityException("Não é possível inserir alternativas iguais as outras" +
                            " e nem iguais ao enunciado da questão!");

            }
        }
    }

    public void verifyAndChangeOrder(Integer orderNumber){
       Optional<Task> task =  taskRepository.findByOrder(orderNumber);

       if (task.isPresent()){
           taskRepository.updateOrder(orderNumber);
           return;
       }

        boolean noTasksFound = taskRepository.existsAnyTask() == 0;
        if (noTasksFound){
            if (!orderNumber.equals(1))
                throw new DataIntegrityException("A ordem deve começar do número 1!");

            return;
        }

        long nextOrderNumber = taskRepository.findLastTaskId() + 1;
        if (nextOrderNumber != orderNumber)
            throw new DataIntegrityException("Não é permitido adicionar uma atividade com ordem " + orderNumber +
                    " pois ainda não existem atividades com ordens " + nextOrderNumber + "!");

    }


}
