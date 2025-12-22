package br.com.alura.AluraFake.controllers;

import br.com.alura.AluraFake.domain.User;
import br.com.alura.AluraFake.domain.enums.Role;
import br.com.alura.AluraFake.dto.user.NewUserDTO;
import br.com.alura.AluraFake.repositories.UserRepository;
import br.com.alura.AluraFake.security.JWTAuthenticationFilter;
import br.com.alura.AluraFake.security.JwtService;
import br.com.alura.AluraFake.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtService jwtService;



    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "paulo@alura.com.br", roles = {"INSTRUCTOR"})
    void newUser__should_return_bad_request_when_email_is_blank() throws Exception {
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setEmail("");
        newUserDTO.setName("Caio Bugorin");
        newUserDTO.setRole(Role.STUDENT);

        mockMvc.perform(post("/user/new")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserDTO)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "paulo@alura.com.br", roles = {"INSTRUCTOR"})
    void newUser__should_return_bad_request_when_email_is_invalid() throws Exception {
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setEmail("");
        newUserDTO.setName("Caio Bugorin");
        newUserDTO.setRole(Role.STUDENT);

        mockMvc.perform(post("/user/new")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    @WithMockUser(username = "paulo@alura.com.br", roles = {"INSTRUCTOR"})
    void newUser__should_return_bad_request_when_email_already_exists() throws Exception {
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setEmail("caio.bugorin@alura.com.br");
        newUserDTO.setName("Caio Bugorin");
        newUserDTO.setRole(Role.STUDENT);

        when(userRepository.existsByEmail(newUserDTO.getEmail())).thenReturn(true);

        mockMvc.perform(post("/user/new")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("email"))
                .andExpect(jsonPath("$.message").value("Email já cadastrado no sistema"));
    }

    @Test
    @WithMockUser(username = "paulo@alura.com.br", roles = {"INSTRUCTOR"})
    void newUser__should_return_created_when_user_request_is_valid() throws Exception {
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setEmail("caio.bugorin@alura.com.br");
        newUserDTO.setName("Caio Bugorin");
        newUserDTO.setRole(Role.STUDENT);

        when(userRepository.existsByEmail(newUserDTO.getEmail())).thenReturn(false);

        mockMvc.perform(post("/user/new")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "paulo@alura.com.br", roles = {"INSTRUCTOR"})
    void listAllUsers__should_list_all_users() throws Exception {
        User user1 = new User("User 1", "user1@test.com",Role.STUDENT);
        User user2 = new User("User 2", "user2@test.com",Role.STUDENT);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/user/all")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("User 1"))
                .andExpect(jsonPath("$[1].name").value("User 2"));
    }

}