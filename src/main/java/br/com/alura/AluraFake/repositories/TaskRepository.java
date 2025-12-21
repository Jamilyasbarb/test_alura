package br.com.alura.AluraFake.repositories;

import br.com.alura.AluraFake.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCourseId(Long courseId);

    @Query("""
            SELECT t FROM Task t WHERE t.course.id = :courseId AND t.statement = :statement
            """)
    List<Task> findTaskAlreadyUtilizedByCourseId(String statement, Long courseId);

    Optional<Task> findByOrder(Integer order);

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE Task SET order_number = order_number + 1 WHERE order_number >= :orderNumber
            """, nativeQuery = true)
    void updateOrder(Integer orderNumber);

    @Query("SELECT COUNT(t) > 0 FROM Task t")
    boolean existsAnyTask();

    @Query(
            value = "SELECT MAX(id) FROM Task",
            nativeQuery = true
    )
    Long findLastTaskId();

    @Query("""
            SELECT COUNT(DISTINCT t.type) = :totalTypes
            FROM Task t WHERE t.course.id = :courseId
            AND t.type IN (:types)
            """)
    boolean verifyTypeOfTasksByCourseId(Long courseId, Integer totalTypes, List<Integer> types);


}
