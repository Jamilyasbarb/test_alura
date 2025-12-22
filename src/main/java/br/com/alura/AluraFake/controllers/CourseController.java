package br.com.alura.AluraFake.controllers;

import br.com.alura.AluraFake.domain.User;
import br.com.alura.AluraFake.dto.course.CourseListItemDTO;
import br.com.alura.AluraFake.dto.course.InstructorCourseReportDTO;
import br.com.alura.AluraFake.repositories.CourseRepository;
import br.com.alura.AluraFake.dto.course.NewCourseDTO;
import br.com.alura.AluraFake.domain.Course;

import br.com.alura.AluraFake.security.UserDetailsServiceImpl;
import br.com.alura.AluraFake.services.CourseService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public CourseController(CourseRepository courseRepository, CourseService courseService,  UserDetailsServiceImpl userDetailsService){
        this.courseRepository = courseRepository;
        this.courseService = courseService;
        this.userDetailsService = userDetailsService;
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Transactional
    @PostMapping("/new")
    public ResponseEntity<Void> createCourse(@Valid @RequestBody NewCourseDTO newCourse) {
        User userAuthenticated = userDetailsService.findByToken();
        Course course = new Course(newCourse.getTitle(), newCourse.getDescription(), userAuthenticated);

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

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/{id}/publish")
    public ResponseEntity<Course> createCourse(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(courseService.publish(id));
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/{instructorId}/list")
    public ResponseEntity<InstructorCourseReportDTO > findCoursesByInstructor(@PathVariable("instructorId") Long instructorId) {
        return ResponseEntity.ok().body(courseService.findByInstructor(instructorId));
    }

}
