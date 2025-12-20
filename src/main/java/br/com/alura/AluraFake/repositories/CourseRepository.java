package br.com.alura.AluraFake.repositories;

import br.com.alura.AluraFake.domain.Course;
import br.com.alura.AluraFake.dto.course.InstructorCourseReportDTOInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long>{

    @Query(value = """
            	SELECT
                    c.id,
                    c.title,
                    c.status,
                    c.publishedAt,
                    COUNT(t.id) AS numberOfQuestion,
                    COUNT(CASE WHEN c.status = 'PUBLISHED' THEN 1 END)
                    OVER () AS numberOfCoursesPublished
                FROM Course c
                LEFT JOIN Task t ON c.id = t.course_id
                WHERE c.instructor_id = :instructorId
                GROUP BY c.id, c.title, c.status, c.publishedAt
            """, nativeQuery = true)
    List<InstructorCourseReportDTOInterface> listCurseReportByInstructor(Long instructorId);
}
