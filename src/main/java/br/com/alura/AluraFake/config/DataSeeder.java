package br.com.alura.AluraFake.config;

import br.com.alura.AluraFake.domain.Course;
import br.com.alura.AluraFake.domain.User;
import br.com.alura.AluraFake.domain.enums.Role;
import br.com.alura.AluraFake.repositories.CourseRepository;
import br.com.alura.AluraFake.repositories.UserRepository;
import br.com.alura.AluraFake.services.DBService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    private final UserRepository userRepository;
    private final DBService dbService;

    public DataSeeder(UserRepository userRepository, DBService dbService) {
        this.userRepository = userRepository;
        this.dbService = dbService;
    }

    @Override
    public void run(String... args) {
        if (!"dev".equals(activeProfile)) return;

        if (userRepository.count() == 0) {
            dbService.initializeDevDatabase();
        }
    }
}