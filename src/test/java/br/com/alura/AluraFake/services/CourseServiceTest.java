package br.com.alura.AluraFake.services;

import br.com.alura.AluraFake.domain.Course;
import br.com.alura.AluraFake.domain.User;
import br.com.alura.AluraFake.domain.enums.CourseStatus;
import br.com.alura.AluraFake.domain.enums.Role;
import br.com.alura.AluraFake.domain.enums.TaskType;
import br.com.alura.AluraFake.dto.course.CourseListDTO;
import br.com.alura.AluraFake.dto.course.InstructorCourseReportDTO;
import br.com.alura.AluraFake.dto.course.InstructorCourseReportDTOInterface;
import br.com.alura.AluraFake.exception.DataIntegrityException;
import br.com.alura.AluraFake.exception.ObjectNotFoundException;
import br.com.alura.AluraFake.mappers.CourseMapper;
import br.com.alura.AluraFake.repositories.CourseRepository;
import br.com.alura.AluraFake.repositories.TaskRepository;
import br.com.alura.AluraFake.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CourseServiceTest {

    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseMapper courseMapper;

    private Course course;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(1L);
        course.setStatus(CourseStatus.BUILDING);
        course.setPublishedAt(LocalDateTime.now());
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void shouldPublishCourseSuccessfully() {

        when(courseRepository.findById(course.getId()))
                .thenReturn(Optional.of(course));

        when(taskRepository.verifyTypeOfTasksByCourseId(
                eq(course.getId()),
                eq(TaskType.values().length),
                anyList()
        )).thenReturn(true);

        when(courseRepository.save(any(Course.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Course result = courseService.publish(course.getId());

        assertNotNull(result);
        assertEquals(CourseStatus.PUBLISHED, result.getStatus());
        assertNotNull(result.getPublishedAt());

        verify(courseRepository).save(course);
    }

    @Test
    void shouldThrowException_whenCourseIsNotBuilding() {
        course.setStatus(CourseStatus.PUBLISHED);

        when(courseRepository.findById(course.getId()))
                .thenReturn(Optional.of(course));

        DataIntegrityException exception = assertThrows(
                DataIntegrityException.class,
                () -> courseService.publish(course.getId())
        );

        assertEquals(
                "Não é possível publicar um curso que não está com o status: Building",
                exception.getMessage()
        );

        verify(courseRepository, never()).save(any());
        verify(taskRepository, never()).verifyTypeOfTasksByCourseId(any(), anyInt(), anyList());
    }

    @Test
    void shouldThrowException_whenCourseDoesNotHaveAllTaskTypes() {

        when(courseRepository.findById(course.getId()))
                .thenReturn(Optional.of(course));

        when(taskRepository.verifyTypeOfTasksByCourseId(
                eq(course.getId()),
                eq(TaskType.values().length),
                anyList()
        )).thenReturn(false);

        DataIntegrityException exception = assertThrows(
                DataIntegrityException.class,
                () -> courseService.publish(course.getId())
        );

        assertEquals(
                "Não é possível publicar um curso que não possua todos os tipos de tarefas! ",
                exception.getMessage()
        );

        verify(courseRepository, never()).save(any());
    }

    //------------------


    @Test
    void shouldReturnInstructorCourseReportSuccessfully() {

        User instructor = new User();
        instructor.setId(1L);
        instructor.setRole(Role.INSTRUCTOR);

        InstructorCourseReportDTOInterface projection =
                mock(InstructorCourseReportDTOInterface.class);
        CourseListDTO courseListDTO = new CourseListDTO(1L, "Title", CourseStatus.BUILDING.name(), LocalDateTime.now().toString(), 1);
        List<CourseListDTO> courseListDTOS = new ArrayList<>();
        courseListDTOS.add(courseListDTO);
        InstructorCourseReportDTO dto = new InstructorCourseReportDTO(courseListDTOS,  2  );

        when(userRepository.findById(instructor.getId()))
                .thenReturn(Optional.of(instructor));

        when(courseRepository.listCurseReportByInstructor(instructor.getId()))
                .thenReturn(List.of(projection));

        when(courseMapper.toCourseListDTO(projection))
                .thenReturn(courseListDTO);

        when(courseMapper.toInstructorCourseReportFromInterface(projection, courseListDTOS))
                .thenReturn(dto);

        InstructorCourseReportDTO result =
                courseService.findByInstructor(instructor.getId());

        assertNotNull(result);


        verify(courseRepository).listCurseReportByInstructor(instructor.getId());
        verify(courseMapper).toInstructorCourseReportFromInterface(projection, courseListDTOS);
    }

    @Test
    void shouldThrowException_whenUserNotFound() {
        Long instructorId = 1L;

        when(userRepository.findById(instructorId))
                .thenReturn(Optional.empty());

        assertThrows(
                ObjectNotFoundException.class,
                () -> courseService.findByInstructor(instructorId)
        );

        verify(courseRepository, never()).listCurseReportByInstructor(any());
        verify(courseMapper, never()).toInstructorCourseReportFromInterface(any(), anyList());
    }

    @Test
    void shouldThrowException_whenUserIsNotInstructor() {
        Long instructorId = 1L;

        User user = new User();
        user.setId(instructorId);
        user.setRole(Role.STUDENT);

        when(userRepository.findById(instructorId))
                .thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> courseService.findByInstructor(instructorId)
        );

        assertEquals(
                "Não foi possível entregar o relatório, pois o usuário não é um Instrutor",
                exception.getMessage()
        );

        verify(courseRepository, never()).listCurseReportByInstructor(any());
        verify(courseMapper, never()).toInstructorCourseReportFromInterface(any(), anyList());
    }
}



