package br.com.alura.AluraFake.dto.task;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateTaskDTO(

        @NotNull(message = "o id do curso não pode ser nulo!")
        Long courseId,

        @Size(min = 4, max = 255, message = "A questão deve ter de 4 à 255 caracteres")
        String statement,

        @NotNull(message = "A ordem não pode ser nula!")
        @PositiveOrZero(message = "A ordem não pode ser negativa")
        Integer order,

        @Valid
        List<TaskOptionDTO> options

) {
}
