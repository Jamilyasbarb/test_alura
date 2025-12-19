package br.com.alura.AluraFake.repositories;

import br.com.alura.AluraFake.domain.TaskOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskOptionsRepository extends JpaRepository<TaskOptions, Long> {
}
