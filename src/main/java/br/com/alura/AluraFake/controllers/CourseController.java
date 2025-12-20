package br.com.alura.AluraFake.controllers;

import br.com.alura.AluraFake.domain.User;
import br.com.alura.AluraFake.dto.course.CourseListItemDTO;
import br.com.alura.AluraFake.repositories.CourseRepository;
import br.com.alura.AluraFake.dto.course.NewCourseDTO;
import br.com.alura.AluraFake.domain.Course;
import br.com.alura.AluraFake.repositories.UserRepository;
import br.com.alura.AluraFake.services.CourseService;
import br.com.alura.AluraFake.util.ErrorItemDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Autowired
    public CourseController(CourseRepository courseRepository, UserRepository userRepository){
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @PostMapping("/new")
    public ResponseEntity createCourse(@Valid @RequestBody NewCourseDTO newCourse) {

        //Caso implemente o bonus, pegue o instrutor logado
        Optional<User> possibleAuthor = userRepository
                .findByEmail(newCourse.getEmailInstructor())
                .filter(User::isInstructor);

        if(possibleAuthor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("emailInstructor", "Usuário não é um instrutor"));
        }

        Course course = new Course(newCourse.getTitle(), newCourse.getDescription(), possibleAuthor.get());

        courseRepository.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<CourseListItemDTO>> createCourse() {
        List<CourseListItemDTO> courses = courseRepository.findAll().stream()
                .map(CourseListItemDTO::new)
                .toList();
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity createCourse(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(courseService.publish(id));
    }

    @GetMapping("/{instructorId}/list")
    public ResponseEntity findCoursesByInstructor(@PathVariable("instructorId") Long instructorId) {
        return ResponseEntity.ok().body(courseService.findByInstructor(instructorId));
    }

}
