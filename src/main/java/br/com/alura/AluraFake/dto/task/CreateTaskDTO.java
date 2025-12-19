package br.com.alura.AluraFake.dto.task;

import br.com.alura.AluraFake.dto.TaskOptionDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateTaskDTO(
        Long courseId,
        @Size(min = 4, max = 255, message = "A questão deve ter de 4 à 255 caracteres")
        String statement,
        Integer order,
        @Valid
        List<TaskOptionDTO> options
) {
}
