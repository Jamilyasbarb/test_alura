package br.com.alura.AluraFake.controllers;

import br.com.alura.AluraFake.config.SecurityConfig;
import br.com.alura.AluraFake.domain.Course;
import br.com.alura.AluraFake.domain.User;
import br.com.alura.AluraFake.domain.enums.CourseStatus;
import br.com.alura.AluraFake.domain.enums.Role;
import br.com.alura.AluraFake.dto.course.NewCourseDTO;
import br.com.alura.AluraFake.exception.DataIntegrityException;
import br.com.alura.AluraFake.exception.ObjectNotFoundException;
import br.com.alura.AluraFake.repositories.CourseRepository;
import br.com.alura.AluraFake.repositories.UserRepository;
import br.com.alura.AluraFake.security.JWTAuthenticationFilter;
import br.com.alura.AluraFake.security.JwtService;
import br.com.alura.AluraFake.security.UserDetailsServiceImpl;
import br.com.alura.AluraFake.services.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CourseController.class)
class CourseControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private CourseService courseService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void newCourseDTO__should_return_bad_request_when_email_is_invalid() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setTitle("Java");
        newCourseDTO.setDescription("Curso de Java");
        newCourseDTO.setEmailInstructor("caio@alura.com.br");

        doThrow(new ObjectNotFoundException("Usuário não encontrado"))
                .when(userDetailsService).findByToken();

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().is4xxClientError());

    }

    @Test
    @WithMockUser(username = "caio@alura.com.br", roles = {"STUDENT"})
    void newCourseDTO__should_return_bad_request_when_email_is_no_instructor() throws Exception {

        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setTitle("Java");
        newCourseDTO.setDescription("Curso de Java");
        newCourseDTO.setEmailInstructor("caio@alura.com.br");

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "paulo@alura.com.br", roles = {"INSTRUCTOR"})
    void newCourseDTO__should_return_created_when_new_course_request_is_valid() throws Exception {

        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setTitle("Java");
        newCourseDTO.setDescription("Curso de Java");
        newCourseDTO.setEmailInstructor("paulo@alura.com.br");

        User user = new User("Paulo","paulo@alura.com.br", Role.INSTRUCTOR);
        doReturn(user).when(userDetailsService).findByToken();

        mockMvc.perform(post("/course/new")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @WithMockUser(username = "paulo@alura.com.br", roles = {"INSTRUCTOR"})
    void listAllCourses__should_list_all_courses() throws Exception {
        User paulo = new User("Paulo", "paulo@alua.com.br", Role.INSTRUCTOR);

        Course java = new Course("Java", "Curso de java", paulo);
        Course hibernate = new Course("Hibernate", "Curso de hibernate", paulo);
        Course spring = new Course("Spring", "Curso de spring", paulo);

        when(courseRepository.findAll()).thenReturn(Arrays.asList(java, hibernate, spring));

        mockMvc.perform(get("/course/all")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Java"))
                .andExpect(jsonPath("$[0].description").value("Curso de java"))
                .andExpect(jsonPath("$[1].title").value("Hibernate"))
                .andExpect(jsonPath("$[1].description").value("Curso de hibernate"))
                .andExpect(jsonPath("$[2].title").value("Spring"))
                .andExpect(jsonPath("$[2].description").value("Curso de spring"));
    }

    @Test
    @WithMockUser(username = "paulo@alura.com.br", roles = {"INSTRUCTOR"})
    void checkSecurityContextDuringRequest() throws Exception {
        mockMvc.perform(get("/course/all")
                        .with(request -> {
                            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                            System.out.println("User during request: " + auth.getName());
                            System.out.println("Roles: " + auth.getAuthorities());
                            return request;
                        }))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "paulo@alura.com.br", roles = {"INSTRUCTOR"})
    void shouldPublishCourseSuccessfully() throws Exception {
        Long courseId = 1L;
        Course course = new Course();
        course.setId(courseId);
        course.setStatus(CourseStatus.PUBLISHED);

        when(courseService.publish(anyLong())).thenReturn(course);

        mockMvc.perform(post("/course/{id}/publish", courseId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(courseService).publish(courseId); // agora vai passar
    }

    @Test
    @WithMockUser(username = "paulo@alura.com.br", roles = {"INSTRUCTOR"})
    void shouldReturnClientError_whenServiceThrowsException() throws Exception {
        Long courseId = 1L;

        when(courseService.publish(courseId))
                .thenThrow(new DataIntegrityException("Erro"));

        mockMvc.perform(post("/course/{id}/publish", courseId))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturnClientErrorWhenServiceThrowsObjectNotFound() throws Exception {
        Long courseId = 1L;

        when(courseService.publish(courseId))
                .thenThrow(new ObjectNotFoundException("Erro"));

        mockMvc.perform(post("/course/{id}/publish", courseId))
                .andExpect(status().is4xxClientError());
    }


}