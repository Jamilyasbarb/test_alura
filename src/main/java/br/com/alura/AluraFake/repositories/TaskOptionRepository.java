package br.com.alura.AluraFake.repositories;

import br.com.alura.AluraFake.domain.TaskOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskOptionRepository extends JpaRepository<TaskOption, Long> {
}
