package br.com.alura.AluraFake.services;

import br.com.alura.AluraFake.domain.Course;
import br.com.alura.AluraFake.domain.User;
import br.com.alura.AluraFake.domain.enums.Role;
import br.com.alura.AluraFake.repositories.CourseRepository;
import br.com.alura.AluraFake.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
public class DBService {

    @Autowired
    private PasswordEncoder pe;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Transactional
    public void initializeDevDatabase(){
        User caio = new User("Caio", "caio@alura.com.br", Role.STUDENT, pe.encode("teste123"));
        User paulo = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR, pe.encode("teste123"));

        userRepository.saveAll(Arrays.asList(caio, paulo));
        courseRepository.save(new Course("Java", "Aprenda Java com Alura", paulo));
    }

}
