package br.com.alura.AluraFake.repositories;

import br.com.alura.AluraFake.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCourseId(Long courseId);

    @Query("""
            SELECT t FROM Task t WHERE t.course.id = :courseId AND t.statement = :statement
            """)
    List<Task> findTaskAlreadyUtilizedByCourseId(String statement, Long courseId);
}
