package br.com.alura.AluraFake.dto.task;

import jakarta.validation.constraints.Size;

public record CreateTaskDTO(
        Long courseId,
        @Size(min = 4, max = 255, message = "A questão deve ter de 4 à 255 caracteres")
        String statement,
        Integer order
) {
}
